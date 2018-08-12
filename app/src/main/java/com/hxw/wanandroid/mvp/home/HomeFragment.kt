package com.hxw.wanandroid.mvp.home

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.hxw.core.base.AbstractFragment
import com.hxw.wanandroid.LoadMoreListener
import com.hxw.wanandroid.R
import com.hxw.wanandroid.binder.ArticleItemViewBinder
import com.hxw.wanandroid.binder.BannerViewBinder
import com.hxw.wanandroid.entity.ArticleData
import com.hxw.wanandroid.entity.ArticleListEntity
import com.hxw.wanandroid.entity.BannerListEntity
import kotlinx.android.synthetic.main.fragment_home.*
import me.drakeet.multitype.Items
import me.drakeet.multitype.MultiTypeAdapter
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.support.closestKodein
import org.kodein.di.jxinject.jx


/**
 * @author hxw on 2018/7/23
 */
class HomeFragment : AbstractFragment(), HomeView, KodeinAware {
    override val kodein: Kodein by closestKodein()
    private val mPresenter: HomePresenter by lazy { kodein.jx.newInstance<HomePresenter>() }
    private val mAdapter = MultiTypeAdapter()
    private val itemData = Items()
    private var curpage: Int = 0
    private val loadMoreListener = object : LoadMoreListener() {
        override fun loadMore() {
            mPresenter.getHomeArticle(curpage++)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun init(savedInstanceState: Bundle?) {
        mPresenter.takeView(this)
        initRecycler()

    }

    override fun onResume() {
        super.onResume()
        mPresenter.getBanner()
        mPresenter.getHomeArticle(curpage)
    }

    private fun initRecycler() {
        mAdapter.register(BannerListEntity::class.java, BannerViewBinder())
        mAdapter.register(ArticleData::class.java, ArticleItemViewBinder())
        recycler_home.layoutManager = LinearLayoutManager(activity)
        recycler_home.adapter = mAdapter
        mAdapter.items = itemData
        mAdapter.notifyDataSetChanged()
        recycler_home.addOnScrollListener(loadMoreListener)
    }

    override fun addArticleData(articleListEntity: ArticleListEntity<ArticleData>) {
        if (articleListEntity.curPage != articleListEntity.pageCount) {
            loadMoreListener.canLoadMore()
        }
        curpage = articleListEntity.curPage
        itemData.addAll(articleListEntity.datas)
        mAdapter.notifyDataSetChanged()
    }

    override fun addBanner(bannerListEntity: BannerListEntity) {
        itemData.add(0, bannerListEntity)
        mAdapter.notifyDataSetChanged()
    }
}