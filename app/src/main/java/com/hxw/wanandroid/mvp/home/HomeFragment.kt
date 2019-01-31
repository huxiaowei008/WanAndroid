package com.hxw.wanandroid.mvp.home

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hxw.core.base.AbstractFragment
import com.hxw.wanandroid.R
import com.hxw.wanandroid.paging.NetworkState
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * @author hxw
 * @date 2018/7/23
 */
class HomeFragment : AbstractFragment() {
    private val mViewModel: HomeViewModel by viewModel()


    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun init(savedInstanceState: Bundle?) {

        initViewPager()
        initRecycler()
        initSwipeToRefresh()
        mViewModel.getBanner()
    }

    private fun initViewPager() {
        vp_banner.adapter = mViewModel.bannerAdapter
        vp_indicator.setViewPager(vp_banner)
    }

    private fun initRecycler() {
        rv_home_article.layoutManager = LinearLayoutManager(activity)
        rv_home_article.adapter = mViewModel.articleAdapter
        mViewModel.pagedList.observe(this, Observer {
            mViewModel.articleAdapter.submitList(it)
        })
    }

    private fun initSwipeToRefresh() {
        refresh.setOnRefreshListener {
            mViewModel.refresh()
        }
        mViewModel.refreshState.observe(this, Observer {
            refresh.isRefreshing = it == NetworkState.LOADING
            if (it.msg != null) {
                toast(it.msg)
            }
        })
    }


}