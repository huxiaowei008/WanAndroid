package com.hxw.wanandroid.mvp.home

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hxw.core.base.AbstractFragment
import com.hxw.core.glide.GlideApp
import com.hxw.wanandroid.Constant
import com.hxw.wanandroid.R
import com.hxw.wanandroid.mvp.CommonViewModel
import com.hxw.wanandroid.mvp.web.AgentWebActivity
import com.hxw.wanandroid.paging.NetworkState
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * @author hxw
 * @date 2018/7/23
 */
class HomeFragment : AbstractFragment() {
    private val mViewModel: HomeViewModel by viewModel()
    private val mCommonViewModel: CommonViewModel by viewModel()
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
        mViewModel.bannerAdapter.setInitView { view, data, _ ->
            GlideApp.with(view)
                    .load(data.imagePath)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_error)
                    .into(view.findViewById(R.id.iv_banner))
            view.setOnClickListener {
                startActivity<AgentWebActivity>(
                        Constant.WEB_URL to data.url
                )
            }
        }
        vp_banner.adapter = mViewModel.bannerAdapter
        vp_indicator.setViewPager(vp_banner)
    }

    private fun initRecycler() {
        mViewModel.articleAdapter.setInitView { view, data, _ ->
            view.findViewById<TextView>(R.id.tv_title).text = data.title
            view.findViewById<TextView>(R.id.tv_time).text = data.niceDate
            view.findViewById<TextView>(R.id.tv_author).text = buildSpannedString {
                append("作者:")
                color(Color.BLACK) {
                    append(data.author)
                }
            }
            view.findViewById<TextView>(R.id.tv_classification).text = buildSpannedString {
                append("分类:")
                color(Color.BLACK) {
                    append("${data.superChapterName}/${data.chapterName}")
                }
            }
            view.findViewById<ImageView>(R.id.iv_favorite).apply {
                imageTintList = ColorStateList.valueOf(ContextCompat
                        .getColor(activity!!, if (data.collect) {
                            R.color.colorPrimary
                        } else {
                            R.color.grey_500
                        }))
            }.setOnClickListener {
                mCommonViewModel.collectArticle(data.id)
            }
            view.setOnClickListener {
                startActivity<AgentWebActivity>(
                        Constant.WEB_URL to data.link
                )
            }
        }
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