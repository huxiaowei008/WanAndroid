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
 * Presenter基类,自带绑定生命周期
 *
 * @author hxw on 2018/6/22.
 */
public class BasePresenter<V extends IView> implements IPresenter<V> {

    protected V mView;
    private CompositeDisposable mCompositeDisposable;

    /**
     * {@link android.app.Activity}的创建生命周期
     * 自己没事别调用
     */
    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        Timber.i("%s - onCreate", this.toString());
    }

    /**
     * {@link android.app.Activity}的销毁生命周期
     * 自己没事别调用
     */
    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        Timber.i("%s - onDestroy", this.toString());
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
        Timber.i("%s - onLifecycleChanged", this.toString());
        if (event == Lifecycle.Event.ON_DESTROY) {
            owner.getLifecycle().removeObserver(this);
        }
    }

    /**
     * 绑定视图,这里是使用的起点,也会自动绑定可订阅的生命周期
     *
     * @param view 被绑定的视图
     */
    @Override
    public void takeView(V view) {
        this.mView = view;
        if (view instanceof LifecycleOwner) {
            ((LifecycleOwner) view).getLifecycle().addObserver(this);
        }
    }

    /**
     * 销毁视图
     */
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
        if (mView == null) {
            throw new NullPointerException("mView == null");
        }
        if (mView instanceof LifecycleOwner) {
            return RxUtils.bindLifecycle((LifecycleOwner) mView);
        } else {
            throw new AssertionError("mView 不是 LifecycleOwner 的子类");
        }
    }

    /**
     * 自带的解除RX订阅的方式
     *
     * @param disposable 可以解除订阅的对象
     */
    protected void addDispose(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }
}
