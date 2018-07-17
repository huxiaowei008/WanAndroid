package com.hxw.core.base

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hxw.core.delegate.AppDelegate
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.androidModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import org.kodein.di.jxinject.jxInjectorModule
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 使用kodein的方式注入
 * @author hxw on 2018/7/17.
 *
 */
abstract class BaseKodeinApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidModule(this@BaseKodeinApplication))
        import(jxInjectorModule)

        bind<Config>() with singleton {
            getConfig()
        }

        bind<Gson>() with singleton {
            val builder = GsonBuilder()
                    .serializeNulls()
            instance<Config>().configGson(instance<Application>(), builder)

            builder.create()
        }

        bind<OkHttpClient>() with singleton {
            val logging = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { })

            logging.level = HttpLoggingInterceptor.Level.BODY
            val builder = OkHttpClient.Builder()
                    .addInterceptor(logging)
            instance<Config>().configOkHttp(instance<Application>(), builder)
            builder.build()
        }
        bind<Retrofit>() with singleton {
            val builder = Retrofit.Builder()
                    .client(instance())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(instance()))
            instance<Config>().configRetrofit(instance<Application>(), builder)
            builder.build()
        }

    }

    private var appDelegate: AppDelegate? = null

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        appDelegate = AppDelegate(this)
        appDelegate!!.attachBaseContext(this)
    }

    override fun onCreate() {
        appDelegate!!.onCreate(this)
        super.onCreate()

    }

    override fun onTerminate() {
        appDelegate!!.onTerminate(this)
        super.onTerminate()
        appDelegate = null
    }

    abstract fun getConfig(): Config
}