package com.hxw.wanandroid.mvp.home

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.hxw.core.base.AbstractFragment
import com.hxw.wanandroid.LoadMoreListener
import com.hxw.wanandroid.R
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


/**
 * @author hxw
 * @date 2018/7/23
 */
class HomeFragment : AbstractFragment() {
    private val mViewModel: HomeViewModel by viewModel()

    private var curpage: Int = 0
    private val loadMoreListener = object : LoadMoreListener() {
        override fun loadMore() {

        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun init(savedInstanceState: Bundle?) {

        initViewPager()
        initRecycler()
        mViewModel.getBanner()
    }

    private fun initViewPager() {
        vp_banner.adapter = mViewModel.bannerAdapter
        vp_indicator.setViewPager(vp_banner)
        vp_indicator.setOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                Timber.tag("ViewPager").i("$position")
            }

        })

    }

    private fun initRecycler() {
//        mAdapter.register(BannerListEntity::class.java, BannerViewBinder())
//        mAdapter.register(ArticleData::class.java, ArticleItemViewBinder())
//        rv_home_article.layoutManager = LinearLayoutManager(activity)
//        rv_home_article.adapter = mAdapter
//        mAdapter.items = itemData
//        mAdapter.notifyDataSetChanged()
//        rv_home_article.addOnScrollListener(loadMoreListener)
    }

//    override fun addArticleData(articleListEntity: ArticleListEntity<ArticleData>) {
//        if (articleListEntity.curPage != articleListEntity.pageCount) {
//            loadMoreListener.canLoadMore()
//        }
//        curpage = articleListEntity.curPage
//        itemData.addAll(articleListEntity.datas)
//        mAdapter.notifyDataSetChanged()
//    }

//    override fun addBanner(bannerListEntity: BannerListEntity) {
//        itemData.add(0, bannerListEntity)
//        mAdapter.notifyDataSetChanged()
//    }
}