package com.hxw.core.base

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hxw.core.utils.StringUtils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein
import org.kodein.di.android.androidModule
import org.kodein.di.android.support.androidSupportModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import org.kodein.di.jxinject.jxInjectorModule
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

/**
 * 框架核心Module提供,和一些kotlin的拓展函数
 * @author hxw on 2018/7/18.
 *
 */
fun coreModule(app: Application, configModule: ConfigModule) = Kodein.Module("MyCoreConfig") {
    importOnce(androidModule(app))
    importOnce(androidSupportModule(app))
    importOnce(jxInjectorModule)

    bind() from  provider { configModule }

    bind<Gson>() with singleton {
        val builder = GsonBuilder()
                .serializeNulls()
        configModule.configGson(app, builder)
        builder.create()
    }

    bind<OkHttpClient>() with singleton {
        val logging = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            var str = it
            if (it.startsWith("{") || it.startsWith("[")) {
                str = it.jsonFormat()
            }
            Timber.tag("OkHttp").i(str)
        })

        logging.level = HttpLoggingInterceptor.Level.BODY
        val builder = OkHttpClient.Builder()
                .addInterceptor(logging)
        configModule.configOkHttp(app, builder)
        builder.build()
    }

    bind<Retrofit>() with singleton {
        val builder = Retrofit.Builder()
                .client(instance())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(instance()))
        configModule.configRetrofit(app, builder)
        builder.build()
    }
}

fun String.jsonFormat() = StringUtils.jsonFormat(this)!!
