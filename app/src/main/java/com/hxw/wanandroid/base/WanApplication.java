package com.hxw.wanandroid.base;

import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.google.gson.GsonBuilder;
import com.hxw.core.base.AbstractApplication;
import com.hxw.core.di.module.ClientModule;
import com.hxw.core.di.module.GlobalConfigModule;
import com.hxw.wanandroid.BuildConfig;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import timber.log.Timber;

/**
 * @author hxw on 2018/5/3.
 */
public class WanApplication extends AbstractApplication {


    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

    }

    @Override
    protected GlobalConfigModule getGlobalConfigModule() {
        return GlobalConfigModule.builder()
                .gsonConfiguration(new ClientModule.GsonConfiguration() {
                    @Override
                    public void configGson(Context context, GsonBuilder builder) {
                        builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
                    }
                })
                .build();
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
//        return DaggerWanComponent
//                .builder()
//                .appComponent(AppUtils.getAppComponent())
//                .build();
        return null;
    }
}
