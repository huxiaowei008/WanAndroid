package com.hxw.wanandroid.base

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.hxw.core.base.ApplicationDelegate
import com.hxw.core.base.coreModule
import com.hxw.wanandroid.BuildConfig
import org.koin.android.ext.android.startKoin
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

        startKoin(this, listOf(coreModule, appModule,viewModel))
    }

    override fun onTerminate() {
        super.onTerminate()
        delegate.onTerminate(this@WanKodeinApplication)
    }
}


