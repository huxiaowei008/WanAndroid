package com.hxw.core.delegate;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;


import com.hxw.core.di.AppComponent;
import com.hxw.core.di.DaggerAppComponent;
import com.hxw.core.di.module.GlobalConfigModule;
import com.hxw.core.integration.ActivityLifecycle;
import com.hxw.core.integration.ConfigModule;
import com.hxw.core.integration.ManifestParser;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author hxw on 2018/5/4.
 */
public class AppDelegate implements AppLifecycle {
    private static AppComponent mAppComponent;

    @Inject
    protected ActivityLifecycle mActivityLifecycle;

    private List<ConfigModule> mModules;
    private List<AppLifecycle> mAppLifecycle = new ArrayList<>();
    private List<Application.ActivityLifecycleCallbacks> mActivityLives = new ArrayList<>();

    public AppDelegate(@NonNull Context context) {
        //用反射, 将 AndroidManifest.xml 中带有 ConfigModule 标签的 class 转成对象集合（List<ConfigModule>）
        this.mModules = new ManifestParser(context).parse();
        //遍历之前获得的集合, 执行每一个 ConfigModule 实现类的某些方法
        for (ConfigModule module : mModules) {
            //将框架外部, 开发者实现的 Application 的生命周期回调 (AppLifecycle) 存入 mAppLifecycle 集合 (此时还未注册回调)
            module.injectAppLifecycle(context, mAppLifecycle);
            //将框架外部, 开发者实现的 Activity 的生命周期回调 (ActivityLifecycleCallbacks) 存入 mActivityLives 集合 (此时还未注册回调)
            module.injectActivityLifecycle(context, mActivityLives);
        }
    }

    public static AppComponent getAppComponent() {
        if (mAppComponent == null) {
            throw new NullPointerException("AppComponent还未初始化,调用太早了!");
        }
        return mAppComponent;
    }

    @Override
    public void attachBaseContext(@NonNull Context base) {
        for (AppLifecycle lifecycle : mAppLifecycle) {
            lifecycle.attachBaseContext(base);
        }
    }

    @Override
    public void onCreate(@NonNull Application application) {
        mAppComponent = DaggerAppComponent
                .builder()
                .application(application)
                .globalConfigModule(getGlobalConfigModule(application, mModules))
                .build();
        mAppComponent.inject(this);

        this.mModules = null;
        //注册框架内部已实现的 Activity 生命周期逻辑
        application.registerActivityLifecycleCallbacks(mActivityLifecycle);
        //注册框架外部, 开发者扩展的 Activity 生命周期逻辑
        for (Application.ActivityLifecycleCallbacks lifecycle : mActivityLives) {
            application.registerActivityLifecycleCallbacks(lifecycle);
        }

        //执行框架外部, 开发者扩展的 App onCreate 逻辑
        for (AppLifecycle lifecycle : mAppLifecycle) {
            lifecycle.onCreate(application);
        }
    }

    @Override
    public void onTerminate(@NonNull Application application) {
        if (mActivityLifecycle != null) {
            application.unregisterActivityLifecycleCallbacks(mActivityLifecycle);
            mActivityLifecycle = null;
        }

        if (mActivityLives != null && mActivityLives.size() > 0) {
            for (Application.ActivityLifecycleCallbacks lifecycle : mActivityLives) {
                application.unregisterActivityLifecycleCallbacks(lifecycle);
            }
        }

        if (mAppLifecycle != null && mAppLifecycle.size() > 0) {
            for (AppLifecycle lifecycle : mAppLifecycle) {
                lifecycle.onTerminate(application);
            }
            mAppLifecycle = null;
        }
    }

    /**
     * 将app的全局配置信息封装进module(使用Dagger注入到需要配置信息的地方)
     * 需要在AndroidManifest中声明{@link ConfigModule}的实现类,和Glide的配置方式相似
     *
     * @return GlobalConfigModule
     */
    private GlobalConfigModule getGlobalConfigModule(Context context, List<ConfigModule> modules) {

        GlobalConfigModule.Builder builder = GlobalConfigModule
                .builder();

        //遍历 ConfigModule 集合, 给全局配置 GlobalConfigModule 添加参数
        for (ConfigModule module : modules) {
            module.applyOptions(context, builder);
        }
        return builder.build();
    }

}
