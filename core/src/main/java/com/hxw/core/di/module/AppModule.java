package com.hxw.lol.di.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * 提供一些框架必须的实例
 *
 * @author hxw on 2018/5/3.
 */
@Module
public abstract class AppModule {

    @Singleton
    @Provides
    static SharedPreferences provideUserSharedPreferences(Application application) {
        return application.getSharedPreferences("UserData", Context.MODE_PRIVATE);
    }

}
