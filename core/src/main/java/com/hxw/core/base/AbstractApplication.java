package com.hxw.core.base;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.hxw.core.integration.ActivityLifecycle;
import com.hxw.core.utils.AppUtils;

import org.kodein.di.Kodein;
import org.kodein.di.KodeinAware;

/**
 * {@link Application}加入生命周期打印的基类
 *
 * @author hxw on 2018/7/18.
 */
public abstract class AbstractApplication extends Application implements KodeinAware {
    protected ActivityLifecycle mActivityLifecycle;

    @Override
    public void onCreate() {
        super.onCreate();
        AppUtils.INSTANCE.setKodein(getKodein());
        mActivityLifecycle = new ActivityLifecycle();
        //注册框架内部已实现的 Activity 生命周期逻辑
        registerActivityLifecycleCallbacks(mActivityLifecycle);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mActivityLifecycle != null) {
            //注销框架内部已实现的 Activity 生命周期逻辑
            unregisterActivityLifecycleCallbacks(mActivityLifecycle);
            mActivityLifecycle = null;
        }
    }
}
