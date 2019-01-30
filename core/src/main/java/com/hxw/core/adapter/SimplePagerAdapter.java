package com.hxw.core.adapter;

import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

/**
 * ViewPager的Adapter简单封装
 *
 * @author hxw
 * @date 2019/1/28
 */
public class SimplePagerAdapter<T> extends PagerAdapter {

    private List<T> mData;
    private final int layoutId;
    private SimpleAdapterInitView<T> initView;
    private boolean isLoop;

    public SimplePagerAdapter(@LayoutRes int layoutId) {
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        if (mData == null || mData.size() == 0) {
            return 0;
        } else {
            if (isLoop) {
                return mData.size() + 2;
            } else {
                return mData.size();
            }
        }
    }

    public int getRealCount() {
        int count = isLoop ? getCount() - 2 : getCount();
        return count < 0 ? 0 : count;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = View.inflate(container.getContext(), layoutId, null);
        if (initView != null) {
            int realPosition = position % getRealCount();
            initView.initView(view, mData.get(realPosition), realPosition);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public SimplePagerAdapter<T> setData(@NonNull List<T> data) {
        this.mData = data;
        return this;
    }

    public SimplePagerAdapter<T> setInitView(SimpleAdapterInitView<T> initView) {
        this.initView = initView;
        return this;
    }

    public SimplePagerAdapter<T> setLoop(boolean loop) {
        isLoop = loop;
        return this;
    }

    public boolean isLoop() {
        return isLoop && getRealCount() != 0;
    }
}
