package com.hxw.wanandroid.mvp.system


import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.hxw.core.base.AbstractFragment
import com.hxw.core.utils.onError
import com.hxw.wanandroid.Constant
import com.hxw.wanandroid.R
import com.hxw.wanandroid.WanApi
import com.hxw.wanandroid.binder.SystemViewBinder
import com.hxw.wanandroid.entity.TreeEntity
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_system.*
import me.drakeet.multitype.Items
import me.drakeet.multitype.MultiTypeAdapter
import org.jetbrains.anko.support.v4.toast
import org.koin.android.ext.android.inject

/**
 * @author hxw on 2018/7/23
 */
class SystemFragment : AbstractFragment() {
    private val api: WanApi by inject()
    private val mAdapter = MultiTypeAdapter()
    private val itemData = Items()
    override fun getLayoutId(): Int {
        return R.layout.fragment_system
    }

    override fun init(savedInstanceState: Bundle?) {
        initRecycler()
    }

    private fun initRecycler() {
        mAdapter.register(TreeEntity::class.java, SystemViewBinder())
        rv_system_article.layoutManager = LinearLayoutManager(activity)
        rv_system_article.adapter = mAdapter
        mAdapter.items = itemData
        mAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        api.getTree()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(this@SystemFragment.scope())
                .subscribe({
                    if (it.errorCode == Constant.NET_SUCCESS) {
                        itemData.addAll(it.data)
                        mAdapter.notifyDataSetChanged()
                    } else {
                        toast(it.errorMsg)
                    }
                }, { it.onError() })
    }
}