package com.hxw.core.mvp;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;

import com.hxw.core.utils.RxUtils;
import com.uber.autodispose.AutoDisposeConverter;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * @author hxw on 2018/6/22.
 */
public class BasePresenter<V extends IView> implements IPresenter<V> {

    protected V mView;
    private LifecycleOwner lifecycleOwner;
    private CompositeDisposable mCompositeDisposable;

    /**
     * 自己没事别调用
     */
    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        Timber.i("%s - onCreate",this.toString());
        this.lifecycleOwner = owner;
    }

    /**
     * 自己没事别调用
     */
    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        Timber.i("%s - onDestroy",this.toString());
        dropView();
    }

    /**
     * 生命周期发生变化时会被调用,调用的顺序是:1、activity的周期(activity的onCreate)
     * 2、presenter的周期(presenter的onCreate)
     * 3、onLifecycleChanged
     * 自己没事别调用
     */
    @Override
    public void onLifecycleChanged(@NonNull LifecycleOwner owner, @NonNull Lifecycle.Event event) {
        Timber.i("%s - onLifecycleChanged",this.toString());
        if (event == Lifecycle.Event.ON_DESTROY) {
            owner.getLifecycle().removeObserver(this);
            lifecycleOwner = null;
        }
    }

    @Override
    public void takeView(V view) {
        this.mView = view;
    }

    @Override
    public void dropView() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
        mView = null;
        mCompositeDisposable = null;
    }

    /**
     * 通过AutoDispose绑定生命周期
     */
    protected <T> AutoDisposeConverter<T> bindLifecycle() {
        if (lifecycleOwner == null) {
            throw new NullPointerException("lifecycleOwner == null");
        }
        return RxUtils.bindLifecycle(lifecycleOwner);
    }

    protected void addDispose(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }
}
