package com.hxw.core.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 用于数据列表变动频繁的异步差异更新Adapter
 *
 * @author hxw
 * @date 2019/1/30
 */
public class AsyncDiffAdapter<T> extends RecyclerView.Adapter<SimpleViewHolder> {

    private final int layoutId;
    private SimpleAdapterInitView<T> initView;
    private final AsyncListDiffer<T> mDiffer;

    public AsyncDiffAdapter(@LayoutRes int layoutId, @NonNull DiffUtil.ItemCallback<T> diffCallback) {
        this.layoutId = layoutId;
        this.mDiffer = new AsyncListDiffer<>(this, diffCallback);
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
            initView.initView(holder.itemView, mDiffer.getCurrentList().get(position), position);
        }
    }

    @Override
    public int getItemCount() {
        return mDiffer.getCurrentList().size();
    }

    public AsyncDiffAdapter<T> setInitView(SimpleAdapterInitView<T> initView) {
        this.initView = initView;
        return this;
    }

    public void submitList(List<T> data) {
        mDiffer.submitList(data);
    }
}
