package com.hxw.wanandroid.base

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.hxw.core.base.ApplicationDelegate
import com.hxw.core.base.ConfigModule
import com.hxw.core.base.coreModule
import com.hxw.wanandroid.BuildConfig
import com.hxw.wanandroid.WanApi
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.module
import retrofit2.Retrofit
import timber.log.Timber

/**
 * @author hxw on 2018/7/17.
 */
class WanKodeinApplication : Application() {
    private val delegate by lazy { ApplicationDelegate() }
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        delegate.attachBaseContext(base)
        MultiDex.install(base)
    }

    override fun onCreate() {
        super.onCreate()
        delegate.onCreate(this@WanKodeinApplication)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin(this, listOf(coreModule, appModule))
    }

    override fun onTerminate() {
        super.onTerminate()
        delegate.onTerminate(this@WanKodeinApplication)
    }
}

val appModule= module {

    single<ConfigModule> { GlobalConfigModule() }

    single { get<Retrofit>().create(WanApi::class.java) }
}
