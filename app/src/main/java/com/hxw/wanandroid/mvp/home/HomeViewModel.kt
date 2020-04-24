package com.hxw.wanandroid.mvp.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.paging.toLiveData
import androidx.recyclerview.widget.DiffUtil
import com.hxw.core.adapter.SimplePagerAdapter
import com.hxw.core.utils.showToast
import com.hxw.wanandroid.Constant
import com.hxw.wanandroid.R
import com.hxw.wanandroid.WanApi
import com.hxw.wanandroid.entity.ArticleEntity
import com.hxw.wanandroid.entity.BannerEntity
import com.hxw.wanandroid.paging.BasePageViewModel
import com.hxw.wanandroid.paging.PageSourceFactory
import com.hxw.wanandroid.paging.SimplePagedListAdapter
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * @author hxw
 * @date 2019/1/25
 */
class HomeViewModel : BasePageViewModel<Int, ArticleEntity>(), KoinComponent {

    private val wanApi: WanApi by inject()
    override val sourceFactory: PageSourceFactory<Int, ArticleEntity> = PageSourceFactory {
        HomeDataSource(wanApi, viewModelScope)
    }

    override val pagedList: LiveData<PagedList<ArticleEntity>> = sourceFactory
        .toLiveData(20)

    val bannerData = liveData<MutableList<BannerEntity>?> {
        emit(getBanner())
    }

    val bannerAdapter: SimplePagerAdapter<BannerEntity> by lazy {
        SimplePagerAdapter<BannerEntity>(R.layout.item_banner)
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

    suspend fun getBanner(): MutableList<BannerEntity>? {
        val result = wanApi.getBanner()
        return if (result.errorCode == Constant.NET_SUCCESS) {
            result.data
        } else {
            showToast(result.errorMsg)
            null
        }
    }
}