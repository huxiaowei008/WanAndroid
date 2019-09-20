package com.hxw.wanandroid.mvp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hxw.core.base.exceptionHandler
import com.hxw.core.utils.showToast


import com.hxw.wanandroid.Constant
import com.hxw.wanandroid.WanApi
import kotlinx.coroutines.launch
import retrofit2.await

/**
 * 通用操作的ViewModel
 * @author hxw
 * @date 2019/2/13
 */
class CommonViewModel(private val api: WanApi) : ViewModel() {

    fun collectArticle(id: Int, action: () -> Unit) {
        viewModelScope.launch(exceptionHandler) {
            val result = api.collect(id).await()
            if (result.errorCode == Constant.NET_SUCCESS) {
                showToast("收藏成功")
                action.invoke()
            } else {
                showToast(result.errorMsg)
            }
        }
    }

    fun unCollectArticle(id: Int, action: () -> Unit) {
        viewModelScope.launch(exceptionHandler) {
            val result = api.unCollect(id).await()
            if (result.errorCode == Constant.NET_SUCCESS) {
                showToast("取消收藏")
                action.invoke()
            } else {
                showToast(result.errorMsg)
            }
        }
    }

}