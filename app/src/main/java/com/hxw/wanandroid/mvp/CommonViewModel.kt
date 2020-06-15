package com.hxw.wanandroid.mvp


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hxw.core.base.exceptionMain
import com.hxw.core.utils.showToast
import com.hxw.wanandroid.Constant
import com.hxw.wanandroid.WanApi
import kotlinx.coroutines.launch

/**
 * 通用操作的ViewModel
 * @author hxw
 * @date 2019/2/13
 */
class CommonViewModel(private val api: WanApi) : ViewModel() {

    fun collectArticle(id: Int, action: () -> Unit) {
        viewModelScope.launch(exceptionMain) {
            val result = api.collect(id)
            if (result.errorCode == Constant.NET_SUCCESS) {
                showToast("收藏成功")
                action.invoke()
            } else {
                showToast(result.errorMsg)
            }
        }
    }

    fun unCollectArticle(id: Int, action: () -> Unit) {
        viewModelScope.launch(exceptionMain) {
            val result = api.unCollect(id)
            if (result.errorCode == Constant.NET_SUCCESS) {
                showToast("取消收藏")
                action.invoke()
            } else {
                showToast(result.errorMsg)
            }
        }
    }

}