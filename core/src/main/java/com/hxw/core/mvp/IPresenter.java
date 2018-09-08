package com.hxw.core.mvp;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.NonNull;

/**
 * 本框架的Presenter基础接口
 *
 * @author hxw on 2018/5/30.
 */
public interface IPresenter<V extends IView> extends LifecycleObserver {

    /**
     * 创建的生命周期
     *
     * @param owner 拥有Android生命周期的类
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate(@NonNull LifecycleOwner owner);

    /**
     * 销毁的生命周期
     *
     * @param owner 拥有Android生命周期的类
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy(@NonNull LifecycleOwner owner);

    /**
     * 各个生命周期的回调
     *
     * @param owner 拥有Android生命周期的类
     * @param event 各个不同的生命周期
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    void onLifecycleChanged(@NonNull LifecycleOwner owner,
                            @NonNull Lifecycle.Event event);

    /**
     * 绑定视图,这里是使用的起点
     *
     * @param view 被绑定的视图
     */
    void takeView(V view);

    /**
     * 销毁视图
     */
    void dropView();
}
