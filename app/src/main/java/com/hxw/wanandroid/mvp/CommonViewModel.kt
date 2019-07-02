package com.hxw.wanandroid.mvp

import androidx.lifecycle.viewModelScope
import com.hxw.core.autodispose.AutoDisposeViewModel
import com.hxw.core.base.subscribe

import com.hxw.core.utils.AppUtils
import com.hxw.wanandroid.Constant
import com.hxw.wanandroid.WanApi

/**
 * 通用操作的ViewModel
 * @author hxw
 * @date 2019/2/13
 */
class CommonViewModel(private val api: WanApi) : AutoDisposeViewModel() {


    fun collectArticle(id: Int, action: () -> Unit) {
        api.collect(id)
            .subscribe(viewModelScope, {
                if (it.errorCode == Constant.NET_SUCCESS) {
                    AppUtils.showToast("收藏成功")
                    action.invoke()
                } else {
                    AppUtils.showToast(it.errorMsg)
                }
            })
    }

    fun unCollectArticle(id: Int, action: () -> Unit) {
        api.unCollect(id)
            .subscribe(viewModelScope, {
                if (it.errorCode == Constant.NET_SUCCESS) {
                    AppUtils.showToast("取消收藏")
                    action.invoke()
                } else {
                    AppUtils.showToast(it.errorMsg)
                }
            })
    }

}