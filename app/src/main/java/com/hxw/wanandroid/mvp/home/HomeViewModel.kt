package com.hxw.wanandroid.mvp.home

import android.widget.TextView
import androidx.paging.toLiveData
import androidx.recyclerview.widget.DiffUtil
import com.hxw.core.adapter.SimplePagerAdapter
import com.hxw.core.autodispose.AutoDisposeViewModel
import com.hxw.core.glide.GlideApp
import com.hxw.core.utils.AppUtils
import com.hxw.wanandroid.Constant
import com.hxw.wanandroid.R
import com.hxw.wanandroid.WanApi
import com.hxw.wanandroid.base.SimplePagedListAdapter
import com.hxw.wanandroid.entity.ArticleEntity
import com.hxw.wanandroid.entity.BannerEntity
import com.uber.autodispose.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * @author hxw
 * @date 2019/1/25
 */
class HomeViewModel(private val wanApi: WanApi) : AutoDisposeViewModel() {

    private val bannerData = mutableListOf<BannerEntity>()
    val factory = HomeDataSourceFactory(wanApi)
    val articleData = factory.toLiveData(20)
    val bannerAdapter by lazy {
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

    val articleAdapter by lazy {
        SimplePagedListAdapter<ArticleEntity>(R.layout.item_article_list, object : DiffUtil.ItemCallback<ArticleEntity>() {
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