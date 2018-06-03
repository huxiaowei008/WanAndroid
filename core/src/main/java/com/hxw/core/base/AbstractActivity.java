package com.hxw.lol.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hxw.lol.delegate.AppDelegate;
import com.hxw.lol.di.AppComponent;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

/**
 * {@link Activity} 基类
 * 如果继承这类使用Presenter,记得添加生命周期订阅  getLifecycle().addObserver(mPresenter);
 * @author hxw on 2018/5/5.
 */
public abstract class AbstractActivity extends AppCompatActivity implements IActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }
        injectActivityComponent(AppDelegate.getAppComponent());
        init(savedInstanceState);
    }

    @Override
    public void injectActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public boolean useFragment() {
        return false;
    }

}
