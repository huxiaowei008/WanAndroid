package com.hxw.core.base;

import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * {@link Fragment} 基础接口
 *
 * @author hxw on 2018/5/5.
 */
public interface IFragment {

    /**
     * 获取布局id
     *
     * @return 返回布局资源id
     */
    @LayoutRes
    int getLayoutId();

    /**
     * 初始化数据
     *
     * @param savedInstanceState 活动重启数据的存储
     */
    void init(@Nullable Bundle savedInstanceState);

}
