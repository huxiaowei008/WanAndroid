package com.hxw.core.base;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;


/**
 * {@link Activity} 基础接口
 *
 * @author hxw
 * @date 2018/5/5
 */
public interface IActivity {

    /**
     * 获取布局id
     *
     * @return 返回布局资源id
     */
    @LayoutRes
    int getLayoutId();

    /**
     * 初始化数据
     *
     * @param savedInstanceState 活动重启数据的存储
     */
    void init(@Nullable Bundle savedInstanceState);

    /**
     * 框架会根据这个属性判断是否注册 {@link FragmentManager.FragmentLifecycleCallbacks}
     *
     * @return false:那意味着这个Activity不需要绑定Fragment,
     * 这个Activity中的Fragment不会执行FragmentLifecycleCallbacks中的代码
     */
    boolean useFragment();
}
