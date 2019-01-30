package com.hxw.wanandroid.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxw.core.adapter.SimpleAdapterInitView;
import com.hxw.core.adapter.SimpleViewHolder;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;

/**
 * @author hxw
 * @date 2019/1/30
 */
public class SimplePagedListAdapter<T> extends PagedListAdapter<T, SimpleViewHolder> {

    private final int layoutId;
    private SimpleAdapterInitView<T> initView;

    public SimplePagedListAdapter(@LayoutRes int layoutId, @NonNull DiffUtil.ItemCallback<T> diffCallback) {
        super(diffCallback);
        this.layoutId = layoutId;
    }

    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new SimpleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position) {
        if (initView != null) {
            initView.initView(holder.itemView, getItem(position), position);
        }
    }

    public SimplePagedListAdapter<T> setInitView(SimpleAdapterInitView<T> initView) {
        this.initView = initView;
        return this;
    }
}
