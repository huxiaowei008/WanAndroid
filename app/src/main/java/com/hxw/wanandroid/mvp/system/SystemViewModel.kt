package com.hxw.wanandroid.mvp.system

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import androidx.recyclerview.widget.DiffUtil
import com.hxw.wanandroid.R
import com.hxw.wanandroid.WanApi
import com.hxw.wanandroid.entity.ArticleEntity
import com.hxw.wanandroid.paging.BasePageViewModel
import com.hxw.wanandroid.paging.PageSourceFactory
import com.hxw.wanandroid.paging.SimplePagedListAdapter

/**
 * @author hxw
 * @date 2019/2/2
 */
class SystemViewModel(wanApi: WanApi, private var cid: Int) :
        BasePageViewModel<Int, ArticleEntity>() {
    override val sourceFactory: PageSourceFactory<Int, ArticleEntity> = PageSourceFactory {
        SystemDataSource(wanApi, cid)
    }

    override val pagedList: LiveData<PagedList<ArticleEntity>> = sourceFactory
            .toLiveData(20)

    val articleAdapter: SimplePagedListAdapter<ArticleEntity> by lazy {
        SimplePagedListAdapter(R.layout.item_article, object : DiffUtil.ItemCallback<ArticleEntity>() {
            override fun areItemsTheSame(oldItem: ArticleEntity, newItem: ArticleEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ArticleEntity, newItem: ArticleEntity): Boolean {
                return oldItem == newItem
            }

        })
    }

    fun change(cid: Int) {
        this.cid = cid
        refresh()
    }
}