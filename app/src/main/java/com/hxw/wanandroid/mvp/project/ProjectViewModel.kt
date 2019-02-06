package com.hxw.wanandroid.mvp.project

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
 * @date 2019/2/5
 */
class ProjectViewModel(wanApi: WanApi) : BasePageViewModel<Int, ArticleEntity>() {
    override val sourceFactory: PageSourceFactory<Int, ArticleEntity> = PageSourceFactory {
       ProjectDataSource(wanApi)
    }

    override val pagedList: LiveData<PagedList<ArticleEntity>> = sourceFactory
            .toLiveData(20)

    val projectAdapter: SimplePagedListAdapter<ArticleEntity> by lazy {
        SimplePagedListAdapter(R.layout.item_project, object : DiffUtil.ItemCallback<ArticleEntity>() {
            override fun areItemsTheSame(oldItem: ArticleEntity, newItem: ArticleEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ArticleEntity, newItem: ArticleEntity): Boolean {
                return oldItem == newItem
            }
        })
    }
}