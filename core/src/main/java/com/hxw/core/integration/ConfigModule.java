package com.hxw.lol.integration;

import android.app.Application;
import android.content.Context;

import com.hxw.lol.delegate.AppLifecycle;
import com.hxw.lol.di.module.GlobalConfigModule;

import java.util.List;

/**
 * {@link ConfigModule} 可以给框架配置一些参数,
 * 需要实现 {@link ConfigModule} 后,在 AndroidManifest 中声明该实现类
 *
 * @author hxw on 2018/5/4.
 */
public interface ConfigModule {
    /**
     * 使用{@link GlobalConfigModule.Builder}给框架配置一些配置参数
     *
     * @param context {@link Context}
     * @param builder {@link GlobalConfigModule.Builder}
     */
    void applyOptions(Context context, GlobalConfigModule.Builder builder);

    /**
     * 使用{@link AppLifecycle}在Application的生命周期中注入一些操作
     *
     * @param context    {@link Context}
     * @param lives {@link AppLifecycle}
     */
    void injectAppLifecycle(Context context, List<AppLifecycle> lives);

    /**
     * 使用{@link Application.ActivityLifecycleCallbacks}在Activity的生命周期中注入一些操作
     *
     * @param context    {@link Context}
     * @param lives {@link Application.ActivityLifecycleCallbacks}
     */
    void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lives);

}
