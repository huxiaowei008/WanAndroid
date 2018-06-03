package com.hxw.core.di;

import android.app.Application;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.hxw.core.delegate.AppDelegate;
import com.hxw.core.di.module.AppModule;
import com.hxw.core.di.module.ClientModule;
import com.hxw.core.di.module.GlobalConfigModule;
import com.hxw.core.integration.AppManager;


import java.io.File;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * 公用组件
 *
 * @author hxw on 2018/5/3.
 */
@Singleton
@Component(modules = {AppModule.class, ClientModule.class, GlobalConfigModule.class})
public interface AppComponent {

    AppManager appManager();

    Application application();

    Gson gson();

    Retrofit retrofit();

    OkHttpClient okHttpClient();

    /**
     * 缓存文件根目录(RxCache和Glide的的缓存都已经作为子文件夹在这个目录里),
     * 应该将所有缓存放到这个根目录里,便于管理和清理,可在GlobeConfigModule里配置
     */
    File cacheFile();

    /**
     * 存放用户数据的SharedPreferences
     */
    SharedPreferences userSharedPreferences();

    void inject(AppDelegate delegate);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        Builder globalConfigModule(GlobalConfigModule globalConfigModule);

        AppComponent build();
    }
}
