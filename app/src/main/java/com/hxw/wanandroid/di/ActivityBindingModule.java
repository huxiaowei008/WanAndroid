package com.hxw.wanandroid.di;

import com.hxw.core.di.ActivityScope;
import com.hxw.wanandroid.mvp.login.LoginActivity;
import com.hxw.wanandroid.mvp.login.RegisterActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * 绑定对应activity
 *
 * @author hxw on 2018/6/1.
 */
@Module
abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector()
    abstract LoginActivity loginActivity();

    @ActivityScope
    @ContributesAndroidInjector()
    abstract RegisterActivity registerActivity();
}
