package com.hxw.core.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v4.content.FileProvider
import android.view.View
import android.widget.Toast
import com.hxw.core.DelegatesExt
import com.hxw.core.integration.AppManager
import org.kodein.di.Kodein
import java.io.File


/**
 * App相关工具类
 *
 * @author hxw on 2018/5/5.
 */
object AppUtils {

    var kodein: Kodein by DelegatesExt.notNullSingleValue()

    @JvmStatic
    fun showSnackBar(message: String) {
        val currentActivity = AppManager.getCurrentActivity()
        if (currentActivity != null) {
            val view = currentActivity
                    .window.decorView.findViewById<View>(android.R.id.content)
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
        }
    }

    @JvmStatic
    fun showToast(message: String) {
        val currentActivity = AppManager.getCurrentActivity()
        if (currentActivity != null) {
            Toast.makeText(currentActivity.applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 启动Activity
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
     * @param outputImage 用来保存图片的文件
     */
    @JvmStatic
    fun openCameraIntent(output: Uri): Intent {
        val openCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // 指定照片保存路径
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, output)
        return openCameraIntent
    }

    /**
     * 获取文件Uri,适配7.0
     */
    @JvmStatic
    fun getUriFormFile(context: Context, file: File): Uri {
        //7.0系统适配
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //转变成Content uri
            FileProvider.getUriForFile(context, context.packageName + ".provider", file)
        } else {
            //file uri
            Uri.fromFile(file)
        }
    }


    /**
     * 获取版本号
     */
    @JvmStatic
    fun getVersionCode(context: Context): Int {
        return try {
            val info = context.packageManager
                    .getPackageInfo(context.packageName, 0)
            info.versionCode
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
