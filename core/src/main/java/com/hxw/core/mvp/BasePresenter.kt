package com.hxw.lol.mvp

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.hxw.lol.utils.RxUtils

import com.uber.autodispose.AutoDispose
import com.uber.autodispose.AutoDisposeConverter
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

import timber.log.Timber

/**
 * @author hxw on 2018/5/30.
 */
class BasePresenter : IPresenter {

    private var lifecycleOwner: LifecycleOwner? = null
    private val mCompositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }
    override fun onCreate(owner: LifecycleOwner) {
        Timber.d("Presenter->onCreate")
        this.lifecycleOwner = owner
    }

    override fun onDestroy(owner: LifecycleOwner) {
        Timber.d("Presenter->onDestroy")
    }

    /**
     * 生命周期发生变化时会被调用,调用的顺序是:1、activity的周期(activity的onCreate)
     * 2、presenter的周期(presenter的onCreate)
     * 3、onLifecycleChanged
     */
    override fun onLifecycleChanged(owner: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            owner.lifecycle.removeObserver(this)
            dispose()
            lifecycleOwner = null
        }
    }

    /**
     * 通过AutoDispose绑定生命周期
     */
    protected fun <T> bindLifecycle(): AutoDisposeConverter<T> {
        if (lifecycleOwner == null) {
            throw NullPointerException("lifecycleOwner == null")
        }
        return RxUtils.bindLifecycle(lifecycleOwner)
    }

    protected fun addDispose(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }

    private fun dispose() {
        mCompositeDisposable.clear()
    }

}
