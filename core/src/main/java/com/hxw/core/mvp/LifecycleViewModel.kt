package com.hxw.core.mvp

import androidx.lifecycle.*

/**
 * 拥有生命周期的ViewModel
 * @author hxw on 2018/12/6.
 *
 */
open class LifecycleViewModel : ViewModel(), LifecycleObserver {
    private var lifecycleOwner: LifecycleOwner? = null

    /**
     * 创建的生命周期
     *
     * @param owner 拥有Android生命周期的类
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(owner: LifecycleOwner) {
        this.lifecycleOwner = owner
    }

    /**
     * 销毁的生命周期
     *
     * @param owner 拥有Android生命周期的类
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(owner: LifecycleOwner) {
        this.lifecycleOwner = null
    }

    /**
     * 各个生命周期的回调
     *
     * @param owner 拥有Android生命周期的类
     * @param event 各个不同的生命周期
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onLifecycleChanged(owner: LifecycleOwner, event: Lifecycle.Event) {

    }

    protected fun getLifecycleOwner(): LifecycleOwner {
        return lifecycleOwner ?: throw NullPointerException("$this's lifecycleOwner is null.")
    }

}