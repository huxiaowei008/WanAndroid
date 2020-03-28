package com.hxw.core.base

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.hxw.core.integration.ActivityLifecycle
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.Module

/**
 * [Application] 的内部实现代理
 *
 * @author hxw
 * @date 2019/1/12
 */
class ApplicationDelegate(private val modules: List<Module>) : IApplication {
    private val mActivityLifecycle: ActivityLifecycle by lazy { ActivityLifecycle() }

    override fun attachBaseContext(base: Context) {

    }

    override fun onCreate(application: Application) {
        //注册框架内部已实现的 Activity 生命周期逻辑
        application.registerActivityLifecycleCallbacks(mActivityLifecycle)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        startKoin {
            androidContext(application)
            androidFileProperties()
            androidLogger()
            modules(modules)
        }
//        RxJavaPlugins.setErrorHandler {
//            if (it is OnErrorNotImplementedException || it is UndeliverableException) {
//                it.cause.onError()
//            } else {
//                it.onError()
//            }
//        }
    }

    override fun onTerminate(application: Application) {
        //注销框架内部已实现的 Activity 生命周期逻辑
        application.unregisterActivityLifecycleCallbacks(mActivityLifecycle)
    }
}
