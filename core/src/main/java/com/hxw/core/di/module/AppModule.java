package com.hxw.core.di.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.hxw.core.integration.ActivityLifecycle;
import com.hxw.core.integration.FragmentLifecycle;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;

/**
 * 提供一些框架必须的实例
 *
 * @author hxw on 2018/5/3.
 */
@Module
public class AppModule {

    @Singleton
    @Provides
    @Named("User")
    static SharedPreferences provideUserSharedPreferences(Application application) {
        return application.getSharedPreferences("UserData", Context.MODE_PRIVATE);
    }

    @Singleton
    @Provides
    static ActivityLifecycle provideActivityLifecycle(Lazy<FragmentLifecycle> fragmentLifecycleLazy) {
        return new ActivityLifecycle(fragmentLifecycleLazy);
    }

    @Singleton
    @Provides
    static FragmentLifecycle provideFragmentLifecycle() {
        return new FragmentLifecycle();
    }

}
