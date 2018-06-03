package com.hxw.lol

import android.net.ParseException
import com.google.gson.JsonParseException
import com.hxw.core.utils.AppUtils
import io.reactivex.Observer
import org.json.JSONException
import retrofit2.HttpException
import timber.log.Timber
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * 实现了错误结果的订阅
 *
 * @author hxw on 2018/5/3.
 */
abstract class AbstractErrorSubscriber<T> : Observer<T> {

    override fun onError(e: Throwable) {
        Timber.tag("Catch-Error").w(e)
        val msg = when (e) {
            is UnknownHostException -> "网络不可用"
            is SocketTimeoutException -> "请求网络超时"
            is HttpException -> convertStatusCode(e)
            is JsonParseException, is ParseException, is JSONException -> "数据解析错误"
            else -> "未知错误"
        }
        AppUtils.showSnackBar(msg)
    }

    private fun convertStatusCode(httpException: HttpException): String {
        return when (httpException.code()) {
            500 -> "服务器发生错误"
            404 -> "请求地址不存在"
            403 -> "请求被服务器拒绝"
            307 -> "请求被重定向到其他页面"
            else -> httpException.message()
        }

    }
}
