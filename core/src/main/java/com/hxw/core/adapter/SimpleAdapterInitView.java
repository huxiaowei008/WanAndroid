package com.hxw.core.adapter;

import android.view.View;

/**
 * @author hxw
 * @date 2019/1/29
 */
public interface SimpleAdapterInitView<T> {
    /**
     * 用于PagerAdapter初始化视图时
     *
     * @param view     构建的视图
     * @param data     数据
     * @param position 位置
     */
    void initView(View view, T data, int position);
}
