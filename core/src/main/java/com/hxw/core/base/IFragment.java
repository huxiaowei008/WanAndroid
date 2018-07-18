package com.hxw.core.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;


/**
 * {@link Fragment} 基础接口
 *
 * @author hxw on 2018/5/5.
 */
public interface IFragment {

    /**
     * @return 返回布局资源ID
     */
    int getLayoutId();

    /**
     * 初始化数据
     *
     * @param savedInstanceState 活动重启数据的存储
     */
    void init(@Nullable Bundle savedInstanceState);

}
