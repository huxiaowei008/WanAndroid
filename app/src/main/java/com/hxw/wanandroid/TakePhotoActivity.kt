package com.hxw.wanandroid

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import com.hxw.core.base.AbstractActivity
import com.hxw.core.utils.AppUtils
import com.hxw.core.utils.FileUtils
import com.hxw.core.utils.PermissionUtils
import kotlinx.android.synthetic.main.activity_take_photo.*
import org.jetbrains.anko.toast
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import java.io.File

/**
 * @author hxw on 2018/7/18.
 *
 */
class TakePhotoActivity : AbstractActivity(), KodeinAware {

    private val permissionCode = 200
    private val cameraCode1 = 1000
    private val cameraCode2 = 1001
    private val pickCode = 1002
    private val cropCode = 1003
    override val kodein: Kodein by closestKodein()
    private lateinit var imageUri: Uri
    private lateinit var saveUri: Uri
    override fun getLayoutId(): Int {
        return R.layout.activity_take_photo
    }

    override fun init(savedInstanceState: Bundle?) {
        btn_camera1.setOnClickListener {
            //启动相机方式1
            PermissionUtils.checkPermissions(this@TakePhotoActivity, object : PermissionUtils.PermissionAction {
                override fun doAction() {
                    openCamera()
                }
            }, permissionCode, Manifest.permission.CAMERA)
        }

        btn_camera2.setOnClickListener {
            /**
             * 启动相机方式2,这方法不去保存图片的输出路径,会通过onActivityResult
             * 中的data返回图片,图片比较模糊
             */
            val openCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(openCameraIntent, cameraCode2)
        }

        btn_pick.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, pickCode)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionCode) {
            permissions.forEachIndexed { index, s ->
                if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                    //申请成功
                    toast("申请成功 requestCode:$requestCode")
                    openCamera()
                } else {
                    toast("申请失败 requestCode:$requestCode")
                    PermissionUtils.somePermissionPermanentlyDenied(this@TakePhotoActivity, s)
                }
            }
        }
    }

    private fun openCamera() {
        imageUri = FileUtils.getUriFormFile(this@TakePhotoActivity,
                File(externalCacheDir, "${System.currentTimeMillis()}image.jpg"))
        val intent = AppUtils.getOpenCameraIntent(imageUri)
        startActivityForResult(intent, cameraCode1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == cameraCode1 && resultCode == Activity.RESULT_OK) {
//            image_test.setImageURI(imageUri)
//            val w = Bitmap.createBitmap(500, 500, Bitmap.Config.RGB_565)
//            w.eraseColor(Color.RED)
//
//            val bitmap = ImageUtils.addWatermark(MediaStore
//                    .Images.Media.getBitmap(contentResolver, imageUri), WatermarkConfig()
//                    .setAlpha(200)
//                    .setXY(0f, 0f)
//                    .setTextColor(Color.BLUE)
//                    .setTextSize(AppUtils.spToPx(this@TakePhotoActivity, 50f).toFloat())
//                    .setText(DateUtils.date2String(Date(), "yyyy-MM-dd HH:mm") + "\n胡晓伟\n高新园区")
//                    .setRecycle(true)
//            )
//
//            val file = File(externalCacheDir, "${System.currentTimeMillis()}压缩.jpg")
//            ImageUtils.compressAndSave(bitmap, file, 20)
//            image_test.setImageURI(Uri.fromFile(file))
            val saveFile = File(externalCacheDir, "${System.currentTimeMillis()}crop.jpg")
            saveUri = Uri.fromFile(saveFile)
            val intent = AppUtils.getCropIntent(this@TakePhotoActivity, imageUri,
                    saveFile)
            startActivityForResult(intent, cropCode)
        } else if (requestCode == cameraCode2 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val bitmap: Bitmap = data.getParcelableExtra("data")
                image_test.setImageBitmap(bitmap)
            }

        } else if (requestCode == pickCode && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val uri = data.data
                val saveFile = File(externalCacheDir, "${System.currentTimeMillis()}crop.jpg")
                saveUri = Uri.fromFile(saveFile)
                val intent = AppUtils.getCropIntent(this@TakePhotoActivity, uri,
                        saveFile)
                startActivityForResult(intent, cropCode)
//                image_test.setImageURI(uri)
            }
        } else if (requestCode == cropCode && resultCode == Activity.RESULT_OK) {
            image_test.setImageURI(saveUri)
        }
    }
}