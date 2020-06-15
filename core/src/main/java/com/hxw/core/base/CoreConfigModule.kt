package com.hxw.core.base

import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hxw.core.integration.HostSelectionInterceptor
import com.hxw.core.utils.jsonFormat
import com.hxw.core.utils.onError
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
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
        getOrNull<ConfigModule>()?.configGson(androidApplication(), builder)
        builder.create()
    }

    single {
        val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                var str = message
                if (message.startsWith("{") || message.startsWith("[")) {
                    str = message.jsonFormat()
                }
                Timber.tag("OkHttp").i(str)
            }
        }).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val builder = OkHttpClient.Builder()
            .addInterceptor(HostSelectionInterceptor)
            .addInterceptor(logging)

        getOrNull<ConfigModule>()?.configOkHttp(androidApplication(), builder)
        builder.build()
    }

    single<Retrofit> {
        val builder = Retrofit.Builder()
            .client(get())
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
//            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(get()))
        getOrNull<ConfigModule>()?.configRetrofit(androidApplication(), builder)
        builder.build()
    }

    factory { PreferenceManager.getDefaultSharedPreferences(androidContext()) }
}

val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
    Timber.i("error in thread ${Thread.currentThread().name}")
    throwable.onError()
}

val exceptionMain = exceptionHandler + Dispatchers.Main
