package com.hxw.core.base

import android.content.Context
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * 可以给框架配置一些参数
 * @author hxw on 2018/7/13.
 *
 */
interface Config {

    fun configGson(context: Context, builder: GsonBuilder)

    fun configOkHttp(context: Context, builder: OkHttpClient.Builder)

    fun configRetrofit(context: Context, builder: Retrofit.Builder)

}

