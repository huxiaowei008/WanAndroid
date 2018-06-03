package com.hxw.wanandroid.di;


import com.hxw.core.di.AppModuleScope;
import com.hxw.wanandroid.WanApi;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * 模块中常用的内容提供
 *
 * @author hxw on 2018/6/1.
 */
@Module
public abstract class WanModule {


    @AppModuleScope
    @Provides
    static WanApi provideWanApi(Retrofit retrofit) {
        return retrofit.create(WanApi.class);
    }
}
