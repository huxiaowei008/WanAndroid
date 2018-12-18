package com.hxw.core.mvp

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.AutoDisposeConverter
import com.uber.autodispose.android.lifecycle.scope
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

/**
 * Presenter基类,自带绑定生命周期
 *
 * @author hxw on 2018/6/22.
 */
open class BasePresenter<V : IView> : IPresenter<V> {

    protected var mView: V? = null
    private val mCompositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

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
        mCompositeDisposable.clear()
        mView = null
    }

    /**
     * 通过AutoDispose绑定生命周期
     */
    protected fun <T> bindLifecycle(): AutoDisposeConverter<T> {
        if (mView == null) {
            throw NullPointerException("mView == null")
        }
        return if (mView is LifecycleOwner) {
            AutoDispose.autoDisposable((mView as LifecycleOwner).scope())
        } else {
            throw AssertionError("mView 不是 LifecycleOwner 的子类")
        }
    }

    /**
     * 自带的解除RX订阅的方式
     *
     * @param disposable 可以解除订阅的对象
     */
    protected fun addDispose(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }
}
