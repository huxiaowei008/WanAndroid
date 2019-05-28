package com.hxw.core.base

import android.app.Application
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hxw.core.integration.HostSelectionInterceptor
import com.hxw.core.utils.jsonFormat
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

/**
 * 框架核心Module提供
 *
 * @author hxw
 * @date 2018/7/18
 */
val coreModule = module {

    single<Gson> {
        val builder = GsonBuilder()
            .serializeNulls()
        get<ConfigModule>().configGson(get<Application>(), builder)
        builder.create()
    }

    single<OkHttpClient> {
        val logging = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { msg ->
            var str = msg
            if (msg.startsWith("{") || msg.startsWith("[")) {
                str = msg.jsonFormat()
            }
            Timber.tag("OkHttp").i(str)
        }).apply {
            level=HttpLoggingInterceptor.Level.BODY
        }
        val builder = OkHttpClient.Builder()
            .addInterceptor(HostSelectionInterceptor)
            .addInterceptor(logging)
        get<ConfigModule>().configOkHttp(get<Application>(), builder)
        builder.build()
    }

    single<Retrofit> {
        val builder = Retrofit.Builder()
            .client(get())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(get()))
        get<ConfigModule>().configRetrofit(get<Application>(), builder)
        builder.build()
    }

    factory { PreferenceManager.getDefaultSharedPreferences(get()) }
}


