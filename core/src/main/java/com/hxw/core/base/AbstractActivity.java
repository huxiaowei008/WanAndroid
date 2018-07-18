package com.hxw.core.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


/**
 * {@link Activity} 基类
 * 如果继承这类使用Presenter,记得添加生命周期订阅  getLifecycle().addObserver(mPresenter);
 *
 * @author hxw on 2018/5/5.
 */
public abstract class AbstractActivity extends AppCompatActivity implements IActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }
        init(savedInstanceState);
    }

    @Override
    public boolean useFragment() {
        return false;
    }

}
