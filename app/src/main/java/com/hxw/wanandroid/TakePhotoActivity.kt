package com.hxw.wanandroid

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import com.hxw.core.WatermarkConfig
import com.hxw.core.base.AbstractActivity
import com.hxw.core.utils.*
import kotlinx.android.synthetic.main.activity_take_photo.*
import org.jetbrains.anko.toast
import java.io.File
import java.util.*

/**
 * @author hxw on 2018/7/18.
 *
 */
class TakePhotoActivity : AbstractActivity() {

    private val permissionCode = 200
    private val cameraCode1 = 1000
    private val cameraCode2 = 1001
    private val pickCode = 1002
    private val cropCode = 1003
    private lateinit var imageUri: Uri
    private lateinit var saveUri: Uri
    override fun getLayoutId(): Int {
        return R.layout.activity_take_photo
    }

    override fun init(savedInstanceState: Bundle?) {
        btn_camera1.setOnClickListener {
            //启动相机方式1
            PermissionUtils.checkPermissions(this@TakePhotoActivity, arrayOf(Manifest.permission.CAMERA),
                    permissionCode, PermissionAction { openCamera() })
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

            val bitmap = ImageUtils.addWatermark(MediaStore
                    .Images.Media.getBitmap(contentResolver, imageUri), WatermarkConfig()
                    .setAlpha(200)
                    .setXY(0, 0)
                    .setTextSize(AppUtils.spToPx(this@TakePhotoActivity, 100f).toFloat())
                    .setText(DateUtils.date2String(Date(), "yyyy-MM-dd HH:mm") + "胡晓伟高新园区32153153313212313515318513515")
                    .setRecycle(true))

            val file = File(externalCacheDir, "${System.currentTimeMillis()}压缩.jpg")
            ImageUtils.compressAndSave(bitmap, file, 20)
            iv_test.setImageURI(Uri.fromFile(file))
//            val saveFile = File(externalCacheDir, "${System.currentTimeMillis()}crop.jpg")
//            saveUri = Uri.fromFile(saveFile)
//            val intent = AppUtils.getCropIntent(this@TakePhotoActivity, imageUri,
//                    saveFile)
//            startActivityForResult(intent, cropCode)
        } else if (requestCode == cameraCode2 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val bitmap: Bitmap = data.getParcelableExtra("data")
                iv_test.setImageBitmap(bitmap)
            }

        } else if (requestCode == pickCode && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val uri = data.data
                val saveFile = File(externalCacheDir, "${System.currentTimeMillis()}crop.jpg")
                saveUri = Uri.fromFile(saveFile)
                val intent = AppUtils.getCropIntent(this@TakePhotoActivity, uri!!,
                        saveFile)
                startActivityForResult(intent, cropCode)
//                image_test.setImageURI(uri)
            }
        } else if (requestCode == cropCode && resultCode == Activity.RESULT_OK) {
            iv_test.setImageURI(saveUri)
        }
    }
}