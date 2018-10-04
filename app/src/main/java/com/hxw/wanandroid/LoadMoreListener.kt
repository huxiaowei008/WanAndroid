package com.hxw.wanandroid

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * recyclerView加载更多监听
 * @author hxw on 2018/7/28
 */
abstract class LoadMoreListener : RecyclerView.OnScrollListener() {
    private var canLoadMore = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is LinearLayoutManager && canLoadMore) {
            val itemCount = layoutManager.getItemCount()//数据总数
            val lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition()//最后可见的数据
            val isBottom = (lastVisiblePosition >= itemCount - 3)
            if (isBottom) {
                canLoadMore = false
                loadMore()
            }
        }
    }

    fun canLoadMore() {
        canLoadMore = true
    }

    internal abstract fun loadMore()

}
