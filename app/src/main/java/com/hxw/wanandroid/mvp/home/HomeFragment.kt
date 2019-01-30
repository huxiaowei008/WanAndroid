package com.hxw.wanandroid.mvp.home

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.hxw.core.base.AbstractFragment
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


    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun init(savedInstanceState: Bundle?) {

        initViewPager()
        initRecycler()
        initListener()
        mViewModel.getBanner()
    }

    private fun initViewPager() {
        vp_banner.adapter = mViewModel.bannerAdapter
        vp_indicator.setViewPager(vp_banner)
        vp_indicator.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
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

        rv_home_article.layoutManager = LinearLayoutManager(activity)
        rv_home_article.adapter = mViewModel.articleAdapter
        mViewModel.articleData.observe(this, Observer {
            mViewModel.articleAdapter.submitList(it)
        })
    }

    private fun initListener() {
        refresh.setOnRefreshListener {
            mViewModel.factory.sourceLiveData.value?.invalidate()
        }
    }


}