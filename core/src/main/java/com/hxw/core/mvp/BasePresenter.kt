package com.hxw.core.mvp

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import timber.log.Timber

/**
 * Presenter基类,自带绑定生命周期
 *
 * @author hxw
 * @date 2018/6/22
 */
@Deprecated("不推荐使用了", ReplaceWith("ViewModel"))
open class BasePresenter<V : IView> : IPresenter<V> {

    protected var mView: V? = null

    /**
     * [android.app.Activity]的创建生命周期
     * 自己没事别调用
     */
    override fun onCreate(owner: LifecycleOwner) {
        Timber.i("%s - onCreate", this.toString())
    }

    /**
     * [android.app.Activity]的销毁生命周期
     * 自己没事别调用
     */
    override fun onDestroy(owner: LifecycleOwner) {
        Timber.i("%s - onDestroy", this.toString())
        dropView()
    }

    /**
     * 生命周期发生变化时会被调用,调用的顺序是:1、activity的周期(activity的onCreate)
     * 2、presenter的周期(presenter的onCreate)
     * 3、onLifecycleChanged
     * 自己没事别调用
     */
    override fun onLifecycleChanged(owner: LifecycleOwner, event: Lifecycle.Event) {
        Timber.i("%s - onLifecycleChanged", this.toString())
        if (event == Lifecycle.Event.ON_DESTROY) {
            owner.lifecycle.removeObserver(this)
        }
    }

    /**
     * 绑定视图,这里是使用的起点,也会自动绑定可订阅的生命周期
     *
     * @param view 被绑定的视图
     */
    override fun takeView(view: V) {
        this.mView = view
        if (view is LifecycleOwner) {
            (view as LifecycleOwner).lifecycle.addObserver(this)
        }
    }

    /**
     * 销毁视图
     */
    override fun dropView() {
        mView = null
    }
}
