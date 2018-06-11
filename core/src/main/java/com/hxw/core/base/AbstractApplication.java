package com.hxw.core.base;


import android.content.Context;

import com.hxw.core.delegate.AppDelegate;

import dagger.android.support.DaggerApplication;

/**
 * Application基类,需要外部实现
 *
 * @author hxw on 2018/6/7.
 */
public abstract class AbstractApplication extends DaggerApplication {

    private AppDelegate appDelegate;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        appDelegate = new AppDelegate(this);
        appDelegate.attachBaseContext(this);
    }

    @Override
    public void onCreate() {
        appDelegate.onCreate(this);
        super.onCreate();

    }

    @Override
    public void onTerminate() {
        appDelegate.onTerminate(this);
        super.onTerminate();
        appDelegate = null;
    }


}
