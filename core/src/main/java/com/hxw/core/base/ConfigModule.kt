package com.hxw.core.base

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
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

    /**
     * 配置 @[Glide] 的自定义参数,此方法在 @[Glide] 初始化时执行(@[Glide]
     * 在第一次被调用时初始化),只会执行一次
     *
     * @param context 上下文
     * @param builder [GlideBuilder] 此类被用来创建 Glide
     */
    fun applyGlideOptions(context: Context, builder: GlideBuilder)
}

