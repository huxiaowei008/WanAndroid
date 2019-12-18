package com.hxw.wanandroid.mvp.project

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.paging.toLiveData
import androidx.recyclerview.widget.DiffUtil
import com.hxw.wanandroid.R
import com.hxw.wanandroid.WanApi
import com.hxw.wanandroid.entity.ArticleEntity
import com.hxw.wanandroid.paging.BasePageViewModel
import com.hxw.wanandroid.paging.PageSourceFactory
import com.hxw.wanandroid.paging.SimplePagedListAdapter
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * @author hxw
 * @date 2019/2/5
 */
class ProjectMoreViewModel : BasePageViewModel<Int, ArticleEntity>(), KoinComponent {

    private val wanApi: WanApi by inject()
    private var cid: Int = 0
    override val sourceFactory: PageSourceFactory<Int, ArticleEntity> = PageSourceFactory {
        ProjectMoreDataSource(wanApi, cid, viewModelScope)
    }

    override val pagedList: LiveData<PagedList<ArticleEntity>> = sourceFactory
        .toLiveData(20)

    val projectAdapter: SimplePagedListAdapter<ArticleEntity> by lazy {
        SimplePagedListAdapter(
            R.layout.item_project,
            object : DiffUtil.ItemCallback<ArticleEntity>() {
                override fun areItemsTheSame(
                    oldItem: ArticleEntity,
                    newItem: ArticleEntity
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: ArticleEntity,
                    newItem: ArticleEntity
                ): Boolean {
                    return oldItem == newItem
                }
            })
    }

    fun changeCid(cid: Int) {
        this.cid = cid
        refresh()
    }
}