package com.hxw.core.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

/**
 * 用于数据列表变动频繁的异步差异更新Adapter
 *
 * @author hxw
 * @date 2019/1/30
 */
class AsyncDiffAdapter<T>(@param:LayoutRes private val layoutId: Int, diffCallback: DiffUtil.ItemCallback<T>) : RecyclerView.Adapter<SimpleViewHolder>() {
    private var initView: ((view: View, data: T, position: Int) -> Unit)? = null
    private val mDiffer: AsyncListDiffer<T> = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return SimpleViewHolder(v)
    }

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        initView?.invoke(holder.itemView, mDiffer.currentList[position], position)
    }

    override fun getItemCount(): Int {
        return mDiffer.currentList.size
    }

    fun setInitView(initView: (view: View, data: T, position: Int) -> Unit): AsyncDiffAdapter<T> {
        this.initView = initView
        return this
    }

    fun submitList(data: List<T>?) {
        mDiffer.submitList(data)
    }
}
