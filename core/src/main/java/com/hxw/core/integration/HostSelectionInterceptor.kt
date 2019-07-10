package com.hxw.core.integration

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 动态变换BaseUrl
 * @author hxw
 * @date 2019/1/25
 */
object HostSelectionInterceptor : Interceptor {

    @Volatile
    private var baseUrl: HttpUrl? = null

    fun setBaseUrl(baseUrl: String) {
        this.baseUrl = HttpUrl.get(baseUrl)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val host = request.url().host()
        val scheme = request.url().scheme()
        val port = request.url().port()
        if (baseUrl != null && "${baseUrl?.scheme()}://${baseUrl?.host()}:${baseUrl?.port()}/" != "$scheme://$host:$port/") {
            val newUrl = request.url().newBuilder()
                .scheme(baseUrl!!.scheme())
                .host(baseUrl!!.host())
                .port(baseUrl!!.port())
                .build()

            request = request.newBuilder()
                .url(newUrl)
                .build()
        }
        return chain.proceed(request)
    }
}