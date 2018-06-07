package com.hxw.core.base;


import com.hxw.core.di.AppComponent;
import com.hxw.core.di.DaggerAppComponent;
import com.hxw.core.di.module.GlobalConfigModule;
import com.hxw.core.integration.ActivityLifecycle;
import com.hxw.core.utils.AppUtils;

import javax.inject.Inject;

import dagger.android.support.DaggerApplication;

/**
 * Application基类,需要外部实现
 *
 * @author hxw on 2018/6/7.
 */
public abstract class AbstractApplication extends DaggerApplication {

    @Inject
    protected ActivityLifecycle mActivityLifecycle;

    @Override
    public void onCreate() {
        AppComponent mAppComponent = DaggerAppComponent
                .builder()
                .application(this)
                .globalConfigModule(getGlobalConfigModule())
                .build();
        AppUtils.setAppComponent(mAppComponent);
        //applicationInjector在super.onCreate()中就会调用到,会在那注入
        super.onCreate();

        //注册框架内部已实现的 Activity 生命周期逻辑
        registerActivityLifecycleCallbacks(mActivityLifecycle);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mActivityLifecycle != null) {
            unregisterActivityLifecycleCallbacks(mActivityLifecycle);
            mActivityLifecycle = null;
        }
    }

    /**
     * 给外部实现内部的全局配置
     *
     * @return 全局配置Module
     */
    protected abstract GlobalConfigModule getGlobalConfigModule();
}
