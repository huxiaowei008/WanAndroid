package com.hxw.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.annotation.Size
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Toast
import com.hxw.core.DelegatesExt
import com.hxw.core.di.AppComponent


/**
 * App相关工具类
 *
 * @author hxw on 2018/5/5.
 */
@SuppressLint("ShowToast")
object AppUtils {
    private val mToast: Toast by lazy {
        Toast.makeText(mAppComponent.application(),
                "", Toast.LENGTH_SHORT)
    }
    private var mAppComponent: AppComponent by DelegatesExt.notNullSingleValue()

    /**
     * 内部调用,外部再调用就会出错
     */
    @JvmStatic
    fun setAppComponent(appComponent: AppComponent) {
        mAppComponent = appComponent
    }

    @JvmStatic
    fun getAppComponent(): AppComponent {
        return mAppComponent
    }

    @JvmStatic
    fun showSnackBar(message: String) {
        val currentActivity = mAppComponent
                .appManager()
                .currentActivity
        if (currentActivity != null) {
            val view = currentActivity
                    .window.decorView.findViewById<View>(android.R.id.content)
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
        }
    }

    @JvmStatic
    fun showToast(message: String) {
        mToast.setText(message)
        mToast.show()
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
     * 检查权限
     * 有权限: PackageManager.PERMISSION_GRANTED
     * 无权限: PackageManager.PERMISSION_DENIED
     *
     * @param context 上下文
     * @param perms   权限
     * @return 是否有权限 `true` 有权限 `false` 无权限
     */
    @JvmStatic
    fun hasPermissions(context: Context,
                       @Size(min = 1) vararg perms: String): Boolean {
        //版本6.0以下不需要请求权限
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        //是否有权限
        for (perm in perms) {
            if (ContextCompat.checkSelfPermission(context, perm) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
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
