package com.hxw.core.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 * @author hxw
 * @date 2019/1/28
 */
class SimpleRecyclerAdapter<T>(@param:LayoutRes private val layoutId: Int) : RecyclerView.Adapter<SimpleViewHolder>() {

    private var mData: List<T>? = null
    private var initView: ((view: View, data: T, position: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return SimpleViewHolder(v)
    }

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        initView?.invoke(holder.itemView, mData!![position], position)
    }

    override fun getItemCount(): Int {
        return if (mData == null) 0 else mData!!.size
    }

    fun setInitView(initView: (view: View, data: T, position: Int) -> Unit): SimpleRecyclerAdapter<T> {
        this.initView = initView
        return this
    }

    fun setData(data: List<T>?): SimpleRecyclerAdapter<T> {
        this.mData = data
        return this
    }

    fun getData(): List<T>? {
        return mData
    }

}
