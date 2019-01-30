package com.hxw.core.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author hxw
 * @date 2019/1/28
 */
public class SimpleRecyclerAdapter<T> extends RecyclerView.Adapter<SimpleViewHolder> {

    private List<T> mData;
    private final int layoutId;
    private SimpleAdapterInitView<T> initView;

    public SimpleRecyclerAdapter(@LayoutRes int layoutId) {
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
            initView.initView(holder.itemView, mData.get(position), position);
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public SimpleRecyclerAdapter<T> setInitView(SimpleAdapterInitView<T> initView) {
        this.initView = initView;
        return this;
    }

    public SimpleRecyclerAdapter<T> setData(@Nullable List<T> data) {
        this.mData = data;
        return this;
    }

    @Nullable
    public List<T> getData() {
        return mData;
    }

}
