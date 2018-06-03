package com.hxw.lol.delegate;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * 用于代理 {@link Application} 的生命周期
 *
 * @author hxw on 2018/5/4.
 */
public interface AppLifecycle {
    void attachBaseContext(@NonNull Context base);

    void onCreate(@NonNull Application application);

    void onTerminate(@NonNull Application application);
}
