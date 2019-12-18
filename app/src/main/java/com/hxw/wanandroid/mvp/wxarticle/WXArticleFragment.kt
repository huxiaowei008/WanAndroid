package com.hxw.wanandroid.mvp.wxarticle

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.hxw.core.base.AbstractFragment
import com.hxw.wanandroid.Constant
import com.hxw.wanandroid.R
import com.hxw.wanandroid.entity.TreeEntity
import com.hxw.wanandroid.mvp.CommonViewModel
import com.hxw.wanandroid.mvp.web.AgentWebActivity
import com.hxw.wanandroid.paging.NetworkState
import kotlinx.android.synthetic.main.fragment_wx_article.*
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast

/**
 * @author hxw
 * @date 2019/2/6
 */
class WXArticleFragment : AbstractFragment() {

    private val mViewModel: WXArticleViewModel by viewModels()
    private val mCommonViewModel: CommonViewModel by viewModels()

    override val layoutId: Int
        get() = R.layout.fragment_wx_article

    override fun init(savedInstanceState: Bundle?) {
        mViewModel.treeData.observe(this, Observer {
            initTab(it)
        })
        mViewModel.getWxPublish()
        initSearchView()
        initSwipeToRefresh()
        initRecyclerView()
    }

    private fun initSearchView() {
        search.isSubmitButtonEnabled = true
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                mViewModel.changeKey(query ?: "")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        search.setOnCloseListener {
            mViewModel.changeKey("")
            return@setOnCloseListener false
        }
    }

    private fun initTab(data: MutableList<TreeEntity>) {
        tab_layout.removeAllTabs()
        data.forEach {
            tab_layout.addTab(tab_layout.newTab().setText(it.name))
        }
        mViewModel.changeId(data[0].id)
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab) {

            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                mViewModel.changeId(data[tab.position].id)
            }

        })
    }

    private fun initRecyclerView() {
        mViewModel.wxAdapter.setInitView { view, data, position ->
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
                        if (data.collect) {
                            mCommonViewModel.unCollectArticle(data.id) {
                                data.collect = false
                                mViewModel.wxAdapter.notifyItemChanged(position)
                            }
                        } else {
                            mCommonViewModel.collectArticle(data.id) {
                                data.collect = true
                                mViewModel.wxAdapter.notifyItemChanged(position)
                            }
                        }

                    }
            view.setOnClickListener {
                startActivity<AgentWebActivity>(
                        Constant.WEB_URL to data.link
                )
            }
        }
        rv_wx_article.layoutManager = LinearLayoutManager(activity)
        rv_wx_article.adapter = mViewModel.wxAdapter
        mViewModel.pagedList.observe(this, Observer {
            mViewModel.wxAdapter.submitList(it)
        })
    }

    private fun initSwipeToRefresh() {
        refresh.setOnRefreshListener { mViewModel.refresh() }
        mViewModel.refreshState.observe(this, Observer {
            refresh.isRefreshing = it == NetworkState.LOADING
            if (it.msg != null) {
                toast(it.msg)
            }
        })
    }

    override fun onDestroyView() {
        tab_layout.clearOnTabSelectedListeners()
        super.onDestroyView()
    }
}