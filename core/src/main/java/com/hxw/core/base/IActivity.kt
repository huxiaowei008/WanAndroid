package com.hxw.core.base

import android.app.Activity
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager


/**
 * [Activity] 基础接口
 *
 * @author hxw
 * @date 2018/5/5
 */
interface IActivity {

    /**
     * 获取布局id
     *
     * @return 返回布局资源id
     */
    @get:LayoutRes
    val layoutId: Int

    /**
     * 初始化数据
     *
     * @param savedInstanceState 活动重启数据的存储
     */
    fun init(savedInstanceState: Bundle?)

    /**
     * 框架会根据这个属性判断是否注册 [FragmentManager.FragmentLifecycleCallbacks]
     *
     * @return false:那意味着这个Activity不需要绑定Fragment,
     * 这个Activity中的Fragment不会执行FragmentLifecycleCallbacks中的代码
     */
    fun useFragment(): Boolean
}
