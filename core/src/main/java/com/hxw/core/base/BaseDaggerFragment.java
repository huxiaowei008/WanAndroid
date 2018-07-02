package com.hxw.core.base;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.hxw.core.mvp.BasePresenter;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;
import dagger.internal.Beta;

/**
 * 使用dagger.android注入的Fragment基类
 *
 * @author hxw on 2018/6/1.
 */
@Beta
public abstract class BaseDaggerFragment<P extends BasePresenter> extends AbstractFragment
        implements HasSupportFragmentInjector {
    @Inject
    protected P mPresenter;
    @Inject
    DispatchingAndroidInjector<Fragment> childFragmentInjector;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        getLifecycle().addObserver(mPresenter);
        super.onAttach(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter = null;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return childFragmentInjector;
    }
}
