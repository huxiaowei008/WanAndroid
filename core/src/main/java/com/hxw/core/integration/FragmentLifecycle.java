package com.hxw.core.integration;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

/**
 * {@link FragmentManager.FragmentLifecycleCallbacks} 默认实现类
 * 实现内部逻辑
 *
 * @author hxw on 2018/5/5.
 */
@Singleton
public final class FragmentLifecycle extends FragmentManager.FragmentLifecycleCallbacks {

    @Inject
    FragmentLifecycle() {
    }

    @Override
    public void onFragmentAttached(FragmentManager fm, Fragment f, Context context) {
        super.onFragmentAttached(fm, f, context);
        Timber.w("%s - onFragmentAttached", f.toString());
    }

    @Override
    public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
        super.onFragmentCreated(fm, f, savedInstanceState);
        Timber.w("%s - onFragmentCreated", f.toString());
    }

    @Override
    public void onFragmentActivityCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
        super.onFragmentActivityCreated(fm, f, savedInstanceState);
        Timber.w("%s - onFragmentActivityCreated", f.toString());
    }

    @Override
    public void onFragmentViewCreated(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
        super.onFragmentViewCreated(fm, f, v, savedInstanceState);
        Timber.w("%s - onFragmentViewCreated", f.toString());
    }

    @Override
    public void onFragmentStarted(FragmentManager fm, Fragment f) {
        super.onFragmentStarted(fm, f);
        Timber.w("%s - onFragmentStarted", f.toString());
    }

    @Override
    public void onFragmentResumed(FragmentManager fm, Fragment f) {
        super.onFragmentResumed(fm, f);
        Timber.w("%s - onFragmentResumed", f.toString());
    }

    @Override
    public void onFragmentPaused(FragmentManager fm, Fragment f) {
        super.onFragmentPaused(fm, f);
        Timber.w("%s - onFragmentPaused", f.toString());
    }

    @Override
    public void onFragmentStopped(FragmentManager fm, Fragment f) {
        super.onFragmentStopped(fm, f);
        Timber.w("%s - onFragmentStopped", f.toString());
    }

    @Override
    public void onFragmentSaveInstanceState(FragmentManager fm, Fragment f, Bundle outState) {
        super.onFragmentSaveInstanceState(fm, f, outState);
        Timber.w("%s - onFragmentSaveInstanceState", f.toString());
    }

    @Override
    public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
        super.onFragmentViewDestroyed(fm, f);
        Timber.w("%s - onFragmentViewDestroyed", f.toString());
    }

    @Override
    public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
        super.onFragmentDestroyed(fm, f);
        Timber.w("%s - onFragmentDestroyed", f.toString());
    }

    @Override
    public void onFragmentDetached(FragmentManager fm, Fragment f) {
        super.onFragmentDetached(fm, f);
        Timber.w("%s - onFragmentDetached", f.toString());
    }
}
