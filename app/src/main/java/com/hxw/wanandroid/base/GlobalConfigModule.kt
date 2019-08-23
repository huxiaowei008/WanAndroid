package com.hxw.wanandroid.base

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.hxw.core.base.ConfigModule
import com.hxw.wanandroid.WanApi
import com.hxw.wanandroid.base.cookies.CookiesManager
import com.hxw.wanandroid.mvp.host.HostSettingActivity
import okhttp3.OkHttpClient
import org.koin.core.KoinComponent
import org.koin.core.get
import retrofit2.Retrofit
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * @author hxw
 * @date 2018/7/18
 */
class GlobalConfigModule : ConfigModule, KoinComponent {
    override fun configGson(context: Context, builder: GsonBuilder) {
        builder.setDateFormat("yyyy-MM-dd HH:mm:ss")
    }

    override fun configOkHttp(context: Context, builder: OkHttpClient.Builder) {
        builder.cookieJar(CookiesManager(context))
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
    }

    override fun configRetrofit(context: Context, builder: Retrofit.Builder) {
        val sp = get<SharedPreferences>()
        val str = sp.getString(HostSettingActivity.IPUSE, WanApi.BASEURL)
        Timber.d(str)
        builder.baseUrl(WanApi.BASEURL)
    }
}
