package com.hxw.core.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import com.hxw.core.di.AppComponent;


/**
 * {@link Activity} 基础接口
 *
 * @author hxw on 2018/5/5.
 */
public interface IActivity {

    /**
     * @return 返回布局资源ID
     */
    int getLayoutId();

    /**
     * 初始化数据
     *
     * @param savedInstanceState 活动重启数据的存储
     */
    void init(@Nullable Bundle savedInstanceState);

    /**
     * 提供 AppComponent (提供所有的单例对象) 给实现类, 进行 Component 依赖
     *
     * @param appComponent {@link AppComponent}
     */
    void injectActivityComponent(@NonNull AppComponent appComponent);

    /**
     * 框架会根据这个属性判断是否注册 {@link FragmentManager.FragmentLifecycleCallbacks}
     *
     * @return false:那意味着这个Activity不需要绑定Fragment,
     * 这个Activity中的Fragment不会执行FragmentLifecycleCallbacks中的代码
     */
    boolean useFragment();
}
