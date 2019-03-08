package com.hxw.wanandroid.mvp.system


import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.internal.FlowLayout
import com.hxw.core.adapter.SimpleRecyclerAdapter
import com.hxw.core.base.AbstractFragment
import com.hxw.core.utils.onError
import com.hxw.wanandroid.Constant
import com.hxw.wanandroid.R
import com.hxw.wanandroid.WanApi
import com.hxw.wanandroid.entity.TreeEntity
import kotlinx.android.synthetic.main.fragment_system.*
import kotlinx.coroutines.launch
import org.jetbrains.anko.support.v4.dip
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import org.koin.android.ext.android.inject
import timber.log.Timber

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
        launch {
            try {
                val deferred = api.getTree()
                val result = deferred.await()
                Timber.i("result in thread ${Thread.currentThread().name}")
                if (result.errorCode == Constant.NET_SUCCESS) {
                    mAdapter.setData(result.data)
                    mAdapter.notifyDataSetChanged()
                } else {
                    toast(result.errorMsg)
                }
            } catch (t: Throwable) {
                Timber.i("error in thread ${Thread.currentThread().name}")
                t.onError()
            }
        }
    }

    private fun initRecycler() {
        mAdapter.setInitView { view, data, _ ->
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