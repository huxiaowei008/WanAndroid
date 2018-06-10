package com.hxw.core.integration;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.hxw.core.base.IActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;
import timber.log.Timber;

/**
 * {@link Application.ActivityLifecycleCallbacks} 默认实现类
 * 实现内部逻辑
 *
 * @author hxw on 2018/5/5.
 */
@Singleton
public final class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    @Inject
    AppManager mAppManager;

    @Inject
    Lazy<FragmentLifecycle> mFragmentLifecycle;

    @Inject
    ActivityLifecycle() {
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Timber.w("%s - onActivityCreated", activity.toString());
        mAppManager.addActivity(activity);

        boolean useFragment = activity instanceof IActivity && ((IActivity) activity).useFragment();
        if (activity instanceof FragmentActivity && useFragment) {
            ((FragmentActivity) activity).getSupportFragmentManager()
                    .registerFragmentLifecycleCallbacks(mFragmentLifecycle.get(), true);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Timber.w("%s - onActivityStarted", activity.toString());
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Timber.w("%s - onActivityResumed", activity.toString());
        mAppManager.setCurrentActivity(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Timber.w("%s - onActivityPaused", activity.toString());
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Timber.w("%s - onActivityStopped", activity.toString());
        if (mAppManager.getCurrentActivity() == activity) {
            mAppManager.setCurrentActivity(null);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Timber.w("%s - onActivitySaveInstanceState", activity.toString());
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Timber.w("%s - onActivityDestroyed", activity.toString());
        mAppManager.removeActivity(activity);
    }
}
