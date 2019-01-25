package com.hxw.core.integration

import okhttp3.Interceptor
import okhttp3.Response

/**
 * 动态变换BaseUrl
 * @author hxw
 * @date 2019/1/25
 */
object HostSelectionInterceptor : Interceptor {

    @Volatile
    private var host: String? = null

    fun setHost(host: String) {
        this.host = host
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (host != null && host != request.url().host()) {
            val newUrl = request.url().newBuilder()
                    .host(host!!)
                    .build()

            request = request.newBuilder()
                    .url(newUrl)
                    .build()
        }
        return chain.proceed(request)
    }
}