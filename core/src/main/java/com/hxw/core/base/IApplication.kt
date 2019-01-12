package com.hxw.core.base

import android.app.Application
import android.content.Context

/**
 * 代理[Application]的生命周期接口
 *
 * @author hxw
 * @date 2019/1/12
 */
interface IApplication {
    /**
     * [Application.attachBaseContext]
     *
     * @param base The new base context for this wrapper.
     */
    fun attachBaseContext(base: Context)

    /**
     * [Application.onCreate]
     *
     * @param application [Application]
     */
    fun onCreate(application: Application)

    /**
     * [Application.onTerminate]
     *
     * @param application [Application]
     */
    fun onTerminate(application: Application)

}
