package com.hxw.wanandroid.mvp.project

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.hxw.core.base.AbstractActivity
import com.hxw.core.base.subscribe
import com.hxw.core.glide.GlideApp
import com.hxw.wanandroid.Constant
import com.hxw.wanandroid.R
import com.hxw.wanandroid.WanApi
import com.hxw.wanandroid.entity.TreeEntity
import com.hxw.wanandroid.mvp.web.AgentWebActivity
import com.hxw.wanandroid.paging.NetworkState
import kotlinx.android.synthetic.main.activtiy_project_more.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author hxw
 * @date 2019/2/5
 */
class ProjectMoreActivity : AbstractActivity() {
    private val api: WanApi by inject()
    private val mViewModel: ProjectMoreViewModel by viewModel()

    override val layoutId: Int
        get() = R.layout.activtiy_project_more

    override fun init(savedInstanceState: Bundle?) {
        setSupportActionBar(tool_title)
        tool_title.setNavigationOnClickListener { finish() }

        api.projectTree
            .subscribe(lifecycle.coroutineScope, { result ->
                if (result.errorCode == Constant.NET_SUCCESS) {
                    initTab(result.data)
                } else {
                    toast(result.errorMsg)
                }
            })

        initSwipeToRefresh()
        initRecyclerView()
    }

    private fun initTab(data: MutableList<TreeEntity>) {
        data.forEach {
            tab_layout.addTab(tab_layout.newTab().setText(it.name))
        }
        mViewModel.changeCid(data[0].id)
        tool_title.title = data[0].name
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab) {

            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                mViewModel.changeCid(data[tab.position].id)
                tool_title.title = data[tab.position].name
            }

        })
    }

    private fun initRecyclerView() {
        mViewModel.projectAdapter.setInitView { view, data, _ ->
            GlideApp.with(view)
                .load(data.envelopePic)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .into(view.findViewById(R.id.iv_project))
            view.findViewById<TextView>(R.id.tv_title).text = data.title
            view.findViewById<TextView>(R.id.tv_des).text = data.desc
            view.findViewById<TextView>(R.id.tv_author).text = data.author
            view.findViewById<TextView>(R.id.tv_time).text = data.niceDate
            view.setOnClickListener {
                startActivity<AgentWebActivity>(
                    Constant.WEB_URL to data.link
                )
            }
        }

        rv_project.layoutManager = LinearLayoutManager(this)
        rv_project.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.set(0, 0, 0, 1)
            }

            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                val paint = Paint()
                paint.color = Color.GRAY
                for (index in 0 until parent.childCount) {
                    val top = parent.getChildAt(index).bottom
                    c.drawRect(0f, top.toFloat(), parent.width.toFloat(), top + 1f, paint)
                }
            }
        })
        rv_project.adapter = mViewModel.projectAdapter
        mViewModel.pagedList.observe(this, Observer {
            mViewModel.projectAdapter.submitList(it)
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

    override fun onDestroy() {
        super.onDestroy()
        tab_layout.clearOnTabSelectedListeners()
    }
}