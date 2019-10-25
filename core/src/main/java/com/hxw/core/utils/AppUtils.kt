package com.hxw.core.utils

import android.content.Context
import android.content.pm.PackageManager
import android.net.ParseException
import android.os.Build
import android.view.View
import com.google.gson.JsonParseException
import com.hxw.core.integration.AppManager
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.toast
import org.json.JSONException
import retrofit2.HttpException
import timber.log.Timber
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


/**
 * App相关工具类
 *
 * @author hxw
 * @date 2018/5/5
 */


/**
 * 通过SnackBar展示信息
 *
 * @param message 信息
 */

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

fun showToast(message: String) {
    val topActivity = AppManager.getTopActivity()
    topActivity?.toast(message)
}

/**
 * 获取版本号
 */

fun Context.getVersionCode(): Long {
    return try {
        val info = this.packageManager
            .getPackageInfo(this.packageName, 0)
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

fun Context.getVersionName(): String {
    return try {
        val info = this.packageManager
            .getPackageInfo(this.packageName, 0)
        info.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        ""
    }
}

/**
 * 错误处理
 */

fun Throwable?.onError() {
    Timber.tag("Catch-Error").e(this)
    val msg = when (this) {
        null -> "无报错信息Throwable==null"
        is UnknownHostException -> "域名解析失败,请检查网络和服务器"
        is SocketTimeoutException -> "请求网络超时"
        is NoRouteToHostException -> "无法连接远程地址与端口"
        is HttpException -> when (this.code()) {
            500 -> "服务器发生错误"
            404 -> "请求地址不存在"
            403 -> "请求被服务器拒绝"
            307 -> "请求被重定向到其他页面"
            else -> this.message()
        }
        is JsonParseException, is ParseException, is JSONException -> "数据解析错误"
        else -> this.message.toString()
    }
    showSnackBar(msg)
}

