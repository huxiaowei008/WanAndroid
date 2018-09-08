package com.hxw.core.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxw.core.utils.PermissionUtils;


/**
 * {@link Fragment} 基类
 * 如果继承这类使用Presenter,记得添加生命周期订阅  getLifecycle().addObserver(mPresenter);
 * (现在已经在{@link com.hxw.core.mvp.BasePresenter}中做好了,记得调用mPresenter.takeView())
 *
 * @author hxw on 2018/5/5.
 */
public abstract class AbstractFragment extends Fragment implements IFragment {

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init(savedInstanceState);
    }

}
