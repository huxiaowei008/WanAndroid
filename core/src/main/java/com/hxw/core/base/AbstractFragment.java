package com.hxw.lol.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxw.lol.delegate.AppDelegate;
import com.hxw.lol.di.AppComponent;

/**
 * {@link Fragment} 基类
 * 如果继承这类使用Presenter,记得添加生命周期订阅  getLifecycle().addObserver(mPresenter);
 *
 * @author hxw on 2018/5/5.
 */
public abstract class AbstractFragment extends Fragment implements IFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        injectFragmentComponent(AppDelegate.getAppComponent());
        init(savedInstanceState);
    }

    @Override
    public void injectFragmentComponent(@NonNull AppComponent appComponent) {

    }

}
