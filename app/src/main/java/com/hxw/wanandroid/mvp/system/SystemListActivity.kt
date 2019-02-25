package com.hxw.wanandroid.mvp.system

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.hxw.core.base.AbstractActivity
import com.hxw.wanandroid.Constant
import com.hxw.wanandroid.R
import com.hxw.wanandroid.entity.TreeEntity
import com.hxw.wanandroid.mvp.CommonViewModel
import com.hxw.wanandroid.mvp.web.AgentWebActivity
import com.hxw.wanandroid.paging.NetworkState
import kotlinx.android.synthetic.main.activity_system_list.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author hxw
 * @date 2019/2/2
 */
class SystemListActivity : AbstractActivity() {
    private val mCommonViewModel: CommonViewModel by viewModel()
    private val systemItem by lazy { intent.getSerializableExtra(Constant.SYSTEM_ITEM) as TreeEntity }
    private val subIndex by lazy { intent.getIntExtra(Constant.SUB_SYSTEM_ITEM, 0) }

    private lateinit var mViewModel: SystemViewModel

    override fun getLayoutId(): Int {
        return R.layout.activity_system_list
    }

    override fun init(savedInstanceState: Bundle?) {
        setSupportActionBar(tool_title)
        tool_title.setNavigationOnClickListener { finish() }

        mViewModel = getViewModel()
        initTabLayout()
        initSwipeToRefresh()
        initRecycler()

    }

    override fun onResume() {
        super.onResume()
        tool_title.title = systemItem.name
    }

    private fun getViewModel(): SystemViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SystemViewModel(get(), systemItem.children[subIndex].id) as T
            }
        }).get(SystemViewModel::class.java)
    }

    private fun initTabLayout() {
        systemItem.children.forEach {
            tab_layout.addTab(tab_layout.newTab().setText(it.name))
        }
        tab_layout.getTabAt(subIndex)?.select()
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab) {

            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                mViewModel.change(systemItem.children[tab.position].id)
            }

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

    private fun initRecycler() {
        mViewModel.articleAdapter.setInitView { view, data, position ->
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
                        .getColor(this@SystemListActivity, if (data.collect) {
                            R.color.colorPrimary
                        } else {
                            R.color.grey_500
                        }))
            }.setOnClickListener {
                if (data.collect) {
                    mCommonViewModel.unCollectArticle(data.id) {
                        data.collect = false
                        mViewModel.articleAdapter.notifyItemChanged(position)
                    }
                } else {
                    mCommonViewModel.collectArticle(data.id) {
                        data.collect = true
                        mViewModel.articleAdapter.notifyItemChanged(position)
                    }
                }

            }
            view.setOnClickListener {
                startActivity<AgentWebActivity>(
                        Constant.WEB_URL to data.link
                )
            }
        }

        rv_system.layoutManager = LinearLayoutManager(this)
        rv_system.adapter = mViewModel.articleAdapter
        mViewModel.pagedList.observe(this, Observer {
            mViewModel.articleAdapter.submitList(it)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        tab_layout.clearOnTabSelectedListeners()
    }
}