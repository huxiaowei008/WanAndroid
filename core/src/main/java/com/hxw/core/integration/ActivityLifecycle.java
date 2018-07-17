package com.hxw.core.integration;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.hxw.core.base.IActivity;

import dagger.Lazy;
import timber.log.Timber;

/**
 * {@link Application.ActivityLifecycleCallbacks} 默认实现类
 * 实现内部逻辑
 *
 * @author hxw on 2018/5/5.
 */
public final class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {

   private Lazy<FragmentLifecycle> mFragmentLifecycle;

    public ActivityLifecycle(Lazy<FragmentLifecycle> fragmentLifecycleLazy) {
        mFragmentLifecycle = fragmentLifecycleLazy;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Timber.i("%s - onActivityCreated", activity.toString());
        AppManager.INSTANCE.addActivity(activity);

        boolean useFragment = activity instanceof IActivity && ((IActivity) activity).useFragment();
        if (activity instanceof FragmentActivity && useFragment) {
            ((FragmentActivity) activity).getSupportFragmentManager()
                    .registerFragmentLifecycleCallbacks(mFragmentLifecycle.get(), true);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Timber.i("%s - onActivityStarted", activity.toString());
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Timber.i("%s - onActivityResumed", activity.toString());
        AppManager.INSTANCE.setCurrentActivity(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Timber.i("%s - onActivityPaused", activity.toString());
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Timber.i("%s - onActivityStopped", activity.toString());
        if (AppManager.INSTANCE.getCurrentActivity() == activity) {
            AppManager.INSTANCE.setCurrentActivity(null);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Timber.i("%s - onActivitySaveInstanceState", activity.toString());
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Timber.i("%s - onActivityDestroyed", activity.toString());
        AppManager.INSTANCE.removeActivity(activity);
    }
}
