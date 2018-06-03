package com.hxw.wanandroid.di;


import com.hxw.core.di.AppComponent;
import com.hxw.core.di.AppModuleScope;
import com.hxw.wanandroid.base.WanApplication;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * @author hxw on 2018/6/1.
 */
@AppModuleScope
@Component(dependencies = {AppComponent.class},
        modules = {AndroidSupportInjectionModule.class,
                ActivityBindingModule.class})
public interface WanComponent extends AndroidInjector<WanApplication> {
}
