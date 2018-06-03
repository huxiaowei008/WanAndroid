package com.hxw.wanandroid.base;

import android.content.Context;

import com.hxw.lol.delegate.AppDelegate;
import com.hxw.wanandroid.di.DaggerWanComponent;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

/**
 * @author hxw on 2018/5/3.
 */
public class WanApplication extends DaggerApplication {

    private AppDelegate appDelegate;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if (appDelegate == null) {
            appDelegate = new AppDelegate(base);
        }
        appDelegate.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        if (appDelegate != null) {
            appDelegate.onCreate(this);
        }
        super.onCreate();

    }

    @Override
    public void onTerminate() {
        if (appDelegate != null) {
            appDelegate.onTerminate(this);
        }
        super.onTerminate();

    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerWanComponent
                .builder()
                .appComponent(AppDelegate.getAppComponent())
                .build();
    }
}
