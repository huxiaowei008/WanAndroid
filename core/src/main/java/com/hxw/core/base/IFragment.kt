package com.hxw.core.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment


/**
 * [Fragment] 基础接口
 *
 * @author hxw
 * @date 2018/5/5
 */
interface IFragment {

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

}
