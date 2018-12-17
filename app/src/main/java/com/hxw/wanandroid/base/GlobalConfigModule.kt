package com.hxw.wanandroid.base

import android.content.Context
import android.content.SharedPreferences

import com.bumptech.glide.GlideBuilder
import com.google.gson.GsonBuilder
import com.hxw.core.base.ConfigModule
import com.hxw.core.utils.AppUtils
import com.hxw.wanandroid.WanApi
import com.hxw.wanandroid.mvp.host.HostSettingActivity
import org.kodein.di.jxinject.Jx

import okhttp3.OkHttpClient
import org.kodein.di.generic.instance
import retrofit2.Retrofit
import timber.log.Timber

/**
 * @author hxw on 2018/7/18.
 */
class GlobalConfigModule : ConfigModule {
    override fun configGson(context: Context, builder: GsonBuilder) {
        builder.setDateFormat("yyyy-MM-dd HH:mm:ss")
    }

    override fun configOkHttp(context: Context, builder: OkHttpClient.Builder) {

    }

    override fun configRetrofit(context: Context, builder: Retrofit.Builder) {
                val sp by AppUtils.kodein.instance<SharedPreferences>()
                val str = sp.getString(HostSettingActivity.IPUSE, WanApi.BASEURL)
                Timber.i(str)
        builder.baseUrl(WanApi.BASEURL)
    }

    override fun applyGlideOptions(context: Context, builder: GlideBuilder) {

    }
}