package com.hxw.wanandroid.mvp.home

import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import androidx.recyclerview.widget.DiffUtil
import com.hxw.core.adapter.SimplePagerAdapter
import com.hxw.core.glide.GlideApp
import com.hxw.core.utils.AppUtils
import com.hxw.wanandroid.Constant
import com.hxw.wanandroid.R
import com.hxw.wanandroid.WanApi
import com.hxw.wanandroid.entity.ArticleEntity
import com.hxw.wanandroid.entity.BannerEntity
import com.hxw.wanandroid.paging.BasePageViewModel
import com.hxw.wanandroid.paging.NetworkState
import com.hxw.wanandroid.paging.PageSourceFactory
import com.hxw.wanandroid.paging.SimplePagedListAdapter
import com.uber.autodispose.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers

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
            .toLiveData(20, fetchExecutor = NetworkState.NETWORK_IO)

    private val bannerData = mutableListOf<BannerEntity>()

    val bannerAdapter: SimplePagerAdapter<BannerEntity> by lazy {
        SimplePagerAdapter<BannerEntity>(R.layout.item_banner)
                .setData(bannerData)
                .setInitView { view, data, position ->
                    GlideApp.with(view)
                            .load(data.imagePath)
                            .placeholder(R.drawable.ic_placeholder)
                            .error(R.drawable.ic_error)
                            .into(view.findViewById(R.id.iv_banner))
                    view.setOnClickListener {
                        AppUtils.showToast("$position")
                    }
                }.setLoop(true)
    }

    val articleAdapter: SimplePagedListAdapter<ArticleEntity> by lazy {
        SimplePagedListAdapter(R.layout.item_article_list, object : DiffUtil.ItemCallback<ArticleEntity>() {
            override fun areItemsTheSame(oldItem: ArticleEntity, newItem: ArticleEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ArticleEntity, newItem: ArticleEntity): Boolean {
                return oldItem == newItem
            }

        }).setInitView { view, data, position ->
            view.findViewById<TextView>(R.id.tv_title).text = data.title
        }
    }

    fun getBanner() {
        wanApi.banner
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(this@HomeViewModel)
                .subscribe {
                    if (it.errorCode == Constant.NET_SUCCESS) {
                        bannerData.addAll(it.data)
                        bannerAdapter.notifyDataSetChanged()
                    } else {
                        AppUtils.showToast(it.errorMsg)
                    }
                }
    }
}