package com.hxw.core.delegate;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * 用于代理 {@link Application} 的生命周期
 *
 * @author hxw on 2018/5/4.
 */
public interface AppLifecycle {
    /**
     * {@link Application} attachBaseContext
     * @param application {@link Application}
     */
    void attachBaseContext(@NonNull Application application);

    /**
     * {@link Application} onCreate
     * @param application {@link Application}
     */
    void onCreate(@NonNull Application application);

    /**
     * {@link Application} onCreate
     * @param application {@link Application}
     */
    void onTerminate(@NonNull Application application);
}
