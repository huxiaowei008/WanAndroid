package com.hxw.wanandroid.mvp.project

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hxw.core.base.AbstractFragment
import com.hxw.core.glide.GlideApp
import com.hxw.wanandroid.Constant
import com.hxw.wanandroid.R
import com.hxw.wanandroid.mvp.web.AgentWebActivity
import com.hxw.wanandroid.paging.NetworkState
import kotlinx.android.synthetic.main.fragment_project.*
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author hxw on 2018/7/23
 */
class ProjectFragment : AbstractFragment() {
    private val mViewModel: ProjectViewModel by viewModel()

    override val layoutId: Int
        get() =  R.layout.fragment_project
    override fun init(savedInstanceState: Bundle?) {

        initRecyclerView()
        initSwipeToRefresh()
        fab_more.setOnClickListener {
            startActivity<ProjectMoreActivity>()
        }
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

        rv_project.layoutManager = LinearLayoutManager(activity)
        rv_project.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
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

}