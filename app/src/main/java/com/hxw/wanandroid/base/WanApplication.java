package com.hxw.wanandroid.base;

import com.hxw.core.base.AbstractApplication;
import com.hxw.core.utils.AppUtils;
import com.hxw.wanandroid.di.DaggerWanComponent;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

/**
 * @author hxw on 2018/5/3.
 */
public class WanApplication extends AbstractApplication {

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerWanComponent
                .builder()
                .appComponent(AppUtils.getAppComponent())
                .build();

    }
}
