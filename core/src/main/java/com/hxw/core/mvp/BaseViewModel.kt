package com.hxw.core.mvp

import com.hxw.core.autodispose.AutoDisposeViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * 自带rx订阅解除的ViewModel
 * @author hxw on 2018/12/6.
 *
 */
open class BaseViewModel : AutoDisposeViewModel() {

    private var mCompositeDisposable: CompositeDisposable? = null

    /**
     * 自带的解除RX订阅的方式
     *
     * @param disposable 可以解除订阅的对象
     */
    protected fun addDispose(disposable: Disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        mCompositeDisposable?.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable?.clear()
        mCompositeDisposable = null
    }

}