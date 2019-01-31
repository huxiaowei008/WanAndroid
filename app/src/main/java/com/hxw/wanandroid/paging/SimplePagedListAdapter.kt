package com.hxw.wanandroid.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.hxw.core.adapter.SimpleViewHolder

/**
 * @author hxw
 * @date 2019/1/30
 */
class SimplePagedListAdapter<T>(@param:LayoutRes private val layoutId: Int, diffCallback: DiffUtil.ItemCallback<T>) : PagedListAdapter<T, SimpleViewHolder>(diffCallback) {
    private var initView: ((view: View, data: T, position: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return SimpleViewHolder(v)
    }

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        initView?.invoke(holder.itemView, getItem(position)!!, position)
    }

    fun setInitView(initView: (view: View, data: T, position: Int) -> Unit): SimplePagedListAdapter<T> {
        this.initView = initView
        return this
    }
}
