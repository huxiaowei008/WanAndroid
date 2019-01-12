package com.hxw.core.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import androidx.core.graphics.green
import com.hxw.core.integration.AppManager
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.gray
import org.jetbrains.anko.opaque
import org.jetbrains.anko.toast
import java.io.File


/**
 * App相关工具类
 *
 * @author hxw on 2018/5/5.
 */
object AppUtils {

    /**
     * 通过SnackBar展示信息
     *
     * @param message 信息
     */
    @JvmStatic
    fun showSnackBar(message: String) {
        val topActivity = AppManager.getTopActivity()
        if (topActivity != null) {
            val view = topActivity
                    .window.decorView.findViewById<View>(android.R.id.content)
            view.snackbar(message)
        }
    }

    /**
     * 通过Toast展示信息
     *
     * @param message 信息
     */
    @JvmStatic
    fun showToast(message: String) {
        val topActivity = AppManager.getTopActivity()
        topActivity?.toast(message)
    }

    /**
     * 启动Activity
     * @param intent 需要启动的意图
     * @param withFinish 是否需要关闭当前的 [Activity]
     */
    @JvmStatic
    fun startActivity(intent: Intent, withFinish: Boolean = false) {
        val currentActivity = AppManager.getCurrentActivity()
        if (currentActivity != null) {
            currentActivity.startActivity(intent)
            if (withFinish) {
                currentActivity.finish()
            }
        }
    }

    /**
     * 获取启动相机意图,需要Manifest.permission.CAMERA权限
     *
     * @param output 用来保存图片的文件Uri
     */
    @JvmStatic
    fun getOpenCameraIntent(output: Uri): Intent {
        val openCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // 指定照片保存路径
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, output)
        return openCameraIntent
    }

    /**
     * 获取剪裁图片的意图
     * @param uri 需要剪裁的图片uri,Android7.0 前是用File Uri,之后是用Content Uri,
     * @param saveFile 保存剪裁后的图片
     */
    @JvmStatic
    fun getCropIntent(context: Context, uri: Uri, saveFile: File): Intent {
        val intent = Intent("com.android.camera.action.CROP")

        val cropUri = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)//为7.0适配
                uri
            }
            "content".equals(uri.scheme, true) -> {
                //相册选择4.4之前返回的uri是File类型的，之后就是content类型的
                //所以在4.4到7.0之间,需要把content 类型的uri转化成file类型的
                Uri.fromFile(File(FileUtils.getImagePath(context, uri, null)))
            }
            else -> {
                uri
            }
        }

        intent.setDataAndType(cropUri, "image/*")
        // 设置裁剪
        //下面这个crop = true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true")
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", 1)
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 400)
        intent.putExtra("outputY", 400)
        //设置输出图片格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        intent.putExtra("noFaceDetection", true)// 取消人脸识别
        //是否将数据保留在Bitmap中返回,也可以说是是否返回数据
        //为false时需和intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri)一同使用
        //为true时数据在onActivityResult的data中返回,获取方式和相机方式2的一样
        intent.putExtra("return-data", false)// true:不返回uri，false：返回uri
        //是否缩放
        intent.putExtra("scale", true)
        //是否是圆形裁剪区域，设置了也不一定有效
//        intent.putExtra("circleCrop", true);

        //return-data为false时使用 输入图片的Uri，指定以后，可以在这个uri获得图片
        // 指定照片保存路径（SD卡）
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(saveFile))
        return intent
    }

    /**
     * 获取系统选择图片的意图
     */
    @JvmStatic
    fun getImagePick(): Intent {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        return intent
    }

    /**
     * 获取版本号
     */
    @JvmStatic
    fun getVersionCode(context: Context): Long {
        return try {
            val info = context.packageManager
                    .getPackageInfo(context.packageName, 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                info.longVersionCode
            } else {
                info.versionCode.toLong()
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            0
        }

    }

    /**
     * 获取版本名
     */
    @JvmStatic
    fun getVersionName(context: Context): String {
        return try {
            val info = context.packageManager
                    .getPackageInfo(context.packageName, 0)
            info.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            ""
        }

    }

    /**
     * dp转px
     *
     * @param dpValue dp值
     * @return px值
     */
    @JvmStatic
    fun dpToPx(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * sp转px
     *
     * @param spValue sp值
     * @return px值
     */
    @JvmStatic
    fun spToPx(context: Context, spValue: Float): Int {
        val scale = context.resources.displayMetrics.scaledDensity
        return (spValue * scale + 0.5f).toInt()
    }
}
