package com.hxw.wanandroid.base;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDelegate;

import com.google.gson.GsonBuilder;
import com.hxw.core.delegate.AppLifecycle;
import com.hxw.core.di.module.ClientModule;
import com.hxw.core.di.module.GlobalConfigModule;
import com.hxw.core.integration.ConfigModule;
import com.hxw.wanandroid.BuildConfig;
import com.hxw.wanandroid.WanApi;

import java.util.List;

import timber.log.Timber;

/**
 * 全局配置类
 *
 * @author hxw on 2018/5/5.
 */
public class GlobalConfiguration implements ConfigModule {
    @Override
    public void applyOptions(Context context, GlobalConfigModule.Builder builder) {
        builder.baseurl(WanApi.BASEURL)
                .gsonConfiguration(new ClientModule.GsonConfiguration() {
                    @Override
                    public void configGson(Context context, GsonBuilder builder) {
                        builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
                    }
                });
    }

    @Override
    public void injectAppLifecycle(Context context, List<AppLifecycle> lives) {
        lives.add(new AppLifecycle() {
            @Override
            public void attachBaseContext(@NonNull Application application) {

            }

            @Override
            public void onCreate(@NonNull Application application) {
                if (BuildConfig.DEBUG) {
                    Timber.plant(new Timber.DebugTree());
                }
                AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
            }

            @Override
            public void onTerminate(@NonNull Application application) {

            }
        });
    }

    @Override
    public void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lives) {

    }
}
