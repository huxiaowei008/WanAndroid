package com.hxw.core.base

import android.content.Context
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * 可以给框架配置一些参数
 *
 * @author hxw
 * @date 2018/7/13
 */
interface ConfigModule {
    /**
     * Gson配置接口
     *
     * @param context {@link Context}
     * @param builder {@link GsonBuilder}
     */
    fun configGson(context: Context, builder: GsonBuilder)

    /**
     * OkHttp配置接口
     *
     * @param context {@link Context}
     * @param builder {@link OkHttpClient.Builder}
     */
    fun configOkHttp(context: Context, builder: OkHttpClient.Builder)

    /**
     * Retrofit配置接口
     *
     * @param context {@link Context}
     * @param builder {@link Retrofit.Builder}
     */
    fun configRetrofit(context: Context, builder: Retrofit.Builder)

}

