package com.hxw.wanandroid.adapter

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.hxw.wanandroid.R
import com.hxw.wanandroid.entity.ArticleData

/**
 * @author hxw on 2018/7/28
 */
class ArticleAdapter : PagedListAdapter<ArticleData, ArticleHolder>(DIFF_CALLBACK) {
    companion object {
        private val DIFF_CALLBACK = object :
                DiffUtil.ItemCallback<ArticleData>() {
            // The ID property identifies when items are the same.
            override fun areItemsTheSame(oldItem: ArticleData, newItem: ArticleData) =
                    oldItem.id == newItem.id

            // Use the "==" operator to know when an item's content changes.
            // Implement equals(), or write custom data comparison logic here.
            override fun areContentsTheSame(
                    oldItem: ArticleData, newItem: ArticleData) = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_article_list, parent, false)
        return ArticleHolder(itemView)
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bindTo(item)
        }
    }


}