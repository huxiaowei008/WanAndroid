package com.hxw.wanandroid.mvp.system

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.hxw.core.integration.AbstractErrorSubscriber
import com.hxw.core.base.AbstractFragment
import com.hxw.core.utils.RxUtils
import com.hxw.wanandroid.Constant
import com.hxw.wanandroid.R
import com.hxw.wanandroid.WanApi
import com.hxw.wanandroid.binder.SystemViewBinder
import com.hxw.wanandroid.entity.BaseEntity
import com.hxw.wanandroid.entity.TreeEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_system.*
import me.drakeet.multitype.Items
import me.drakeet.multitype.MultiTypeAdapter
import org.jetbrains.anko.support.v4.toast
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.support.closestKodein
import org.kodein.di.generic.instance

/**
 * @author hxw on 2018/7/23
 */
class SystemFragment : AbstractFragment(), KodeinAware {
    override val kodein: Kodein by closestKodein()
    private val api: WanApi by instance()
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
                .`as`(RxUtils.bindLifecycle<BaseEntity<List<TreeEntity>>>(this@SystemFragment))
                .subscribe(object : AbstractErrorSubscriber<BaseEntity<List<TreeEntity>>>() {
                    override fun onNext(t: BaseEntity<List<TreeEntity>>) {
                        if (t.errorCode == Constant.NET_SUCCESS) {
                            itemData.addAll(t.data)
                            mAdapter.notifyDataSetChanged()
                        } else {
                            toast(t.errorMsg)
                        }
                    }
                })
    }
}