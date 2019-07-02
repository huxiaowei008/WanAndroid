package com.hxw.wanandroid.mvp.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.paging.toLiveData
import androidx.recyclerview.widget.DiffUtil
import com.hxw.core.adapter.SimplePagerAdapter
import com.hxw.core.base.subscribe
import com.hxw.core.utils.AppUtils
import com.hxw.wanandroid.Constant
import com.hxw.wanandroid.R
import com.hxw.wanandroid.WanApi
import com.hxw.wanandroid.entity.ArticleEntity
import com.hxw.wanandroid.entity.BannerEntity
import com.hxw.wanandroid.paging.BasePageViewModel
import com.hxw.wanandroid.paging.PageSourceFactory
import com.hxw.wanandroid.paging.SimplePagedListAdapter

/**
 * @author hxw
 * @date 2019/1/25
 */
class HomeViewModel(private val wanApi: WanApi) :
    BasePageViewModel<Int, ArticleEntity>() {
    override val sourceFactory: PageSourceFactory<Int, ArticleEntity> = PageSourceFactory {
        HomeDataSource(wanApi)
    }

    override val pagedList: LiveData<PagedList<ArticleEntity>> = sourceFactory
        .toLiveData(20)

    private val bannerData = mutableListOf<BannerEntity>()

    val bannerAdapter: SimplePagerAdapter<BannerEntity> by lazy {
        SimplePagerAdapter<BannerEntity>(R.layout.item_banner)
            .setData(bannerData)
            .setLoop(true)
    }

    val articleAdapter: SimplePagedListAdapter<ArticleEntity> by lazy {
        SimplePagedListAdapter(
            R.layout.item_article,
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

    fun getBanner() {
        wanApi.banner
            .subscribe(viewModelScope, {
                if (it.errorCode == Constant.NET_SUCCESS) {
                    bannerData.addAll(it.data)
                    bannerAdapter.notifyDataSetChanged()
                } else {
                    AppUtils.showToast(it.errorMsg)
                }
            })
    }
}