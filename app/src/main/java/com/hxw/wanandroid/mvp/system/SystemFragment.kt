package com.hxw.wanandroid.mvp.system


import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.internal.FlowLayout
import com.hxw.core.adapter.SimpleRecyclerAdapter
import com.hxw.core.base.AbstractFragment
import com.hxw.wanandroid.Constant
import com.hxw.wanandroid.R
import com.hxw.wanandroid.WanApi
import com.hxw.wanandroid.entity.TreeEntity
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_system.*
import org.jetbrains.anko.support.v4.dip
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import org.koin.android.ext.android.inject

/**
 * @author hxw on 2018/7/23
 */
class SystemFragment : AbstractFragment() {
    private val api: WanApi by inject()
    private val mAdapter by lazy {
        SimpleRecyclerAdapter<TreeEntity>(R.layout.item_system)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_system
    }

    override fun init(savedInstanceState: Bundle?) {
        initRecycler()
        api.getTree()
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(this@SystemFragment.scope())
                .subscribe {
                    if (it.errorCode == Constant.NET_SUCCESS) {
                        mAdapter.setData(it.data)
                        mAdapter.notifyDataSetChanged()
                    } else {
                        toast(it.errorMsg)
                    }
                }
    }

    private fun initRecycler() {
        mAdapter.setInitView { view, data, position ->
            view.findViewById<TextView>(R.id.tv_name).text = data.name
            val flow = view.findViewById<FlowLayout>(R.id.flow_layout)
            data.children.forEachIndexed { index, treeEntity ->
                val textView = TextView(flow.context)
                textView.setBackgroundResource(R.drawable.bg_shape_grey)
                val size = dip(4)
                textView.setPadding(size * 2, size, size * 2, size)
                textView.text = treeEntity.name
                textView.setOnClickListener {
                    startActivity<SystemListActivity>(
                            Constant.SYSTEM_ITEM to data,
                            Constant.SUB_SYSTEM_ITEM to index
                    )
                }
                flow.addView(textView)
            }

            view.setOnClickListener {
                startActivity<SystemListActivity>(
                        Constant.SYSTEM_ITEM to data
                )
            }
        }
        rv_system.layoutManager = LinearLayoutManager(activity)
        rv_system.adapter = mAdapter
    }
}