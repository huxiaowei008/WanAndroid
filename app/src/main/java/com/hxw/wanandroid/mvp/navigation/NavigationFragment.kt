package com.hxw.wanandroid.mvp.navigation

import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.FlowLayout
import com.google.android.material.tabs.TabLayout
import com.hxw.core.adapter.SimpleRecyclerAdapter
import com.hxw.core.base.AbstractFragment
import com.hxw.wanandroid.Constant
import com.hxw.wanandroid.R
import com.hxw.wanandroid.WanApi
import com.hxw.wanandroid.entity.NaviEntity
import com.hxw.wanandroid.mvp.web.AgentWebActivity
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_navigation.*
import org.jetbrains.anko.support.v4.dip
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import org.koin.android.ext.android.inject
import kotlin.random.Random

/**
 * @author hxw on 2018/7/23
 */
class NavigationFragment : AbstractFragment() {
    private val api: WanApi by inject()
    private val mAdapter by lazy {
        SimpleRecyclerAdapter<NaviEntity>(R.layout.item_navigation)
    }
    private val mManager by lazy { LinearLayoutManager(activity) }
    private var needScroll = false
    override fun getLayoutId(): Int {
        return R.layout.fragment_navigation
    }

    override fun init(savedInstanceState: Bundle?) {
        initRecycler()
        api.navi.observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(this@NavigationFragment.scope())
            .subscribe {
                if (it.errorCode == Constant.NET_SUCCESS) {
                    initTabLayout(it.data)
                    mAdapter.setData(it.data)
                    mAdapter.notifyDataSetChanged()
                } else {
                    toast(it.errorMsg)
                }
            }
    }

    private fun initTabLayout(data: List<NaviEntity>) {
        data.forEach {
            tab_layout.addTab(tab_layout.newTab().setText(it.name))
        }
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab) {

            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                scrollToPosition(tab.position)
            }

        })
    }

    private fun scrollToPosition(position: Int) {
        val first = mManager.findFirstVisibleItemPosition()
        val last = mManager.findLastVisibleItemPosition()
        if (position < first) {
            rv_list.smoothScrollToPosition(position)
        } else if (position <= last) {
            val move = position - first
            if (move >= 0 && move < rv_list.childCount) {
                val top = rv_list.getChildAt(move).top
                rv_list.smoothScrollBy(0, top)
            }
        } else {
            rv_list.smoothScrollToPosition(position)
            needScroll = true
        }
    }

    private fun initRecycler() {
        val color = intArrayOf(
            ContextCompat.getColor(activity!!, R.color.red_500),
            ContextCompat.getColor(activity!!, R.color.orange_500),
            ContextCompat.getColor(activity!!, R.color.amber_500),
            ContextCompat.getColor(activity!!, R.color.green_500),
            ContextCompat.getColor(activity!!, R.color.blue_500),
            ContextCompat.getColor(activity!!, R.color.indigo_500),
            ContextCompat.getColor(activity!!, R.color.purple_500)
        )
        mAdapter.setInitView { view, data, _ ->
            view.findViewById<TextView>(R.id.tv_name).text = data.name
            val flow = view.findViewById<FlowLayout>(R.id.flow_layout)
            data.articles.forEach {
                val textView = TextView(flow.context)
                textView.setBackgroundResource(R.drawable.bg_shape_grey)
                val size = dip(4)
                textView.setPadding(size * 2, size, size * 2, size)
                textView.text = it.title
                textView.setTextColor(color[Random.nextInt(7)])
                textView.setOnClickListener { v ->
                    startActivity<AgentWebActivity>(
                        Constant.WEB_URL to it.link
                    )
                }
                flow.addView(textView)
            }
        }

        rv_list.layoutManager = mManager
        rv_list.adapter = mAdapter
        rv_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val first = mManager.findFirstVisibleItemPosition()
                    val tab = tab_layout.getTabAt(first)
                    if (tab?.isSelected != true) {
                        tab?.select()
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (needScroll) {
                    needScroll = false
                    scrollToPosition(tab_layout.selectedTabPosition)
                }
            }
        })

    }

    override fun onDestroyView() {
        tab_layout.clearOnTabSelectedListeners()
        super.onDestroyView()
    }
}