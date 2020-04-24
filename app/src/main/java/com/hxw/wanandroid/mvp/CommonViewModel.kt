package com.hxw.wanandroid.mvp


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hxw.core.base.exceptionHandler
import com.hxw.core.utils.showToast
import com.hxw.wanandroid.Constant
import com.hxw.wanandroid.WanApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * 通用操作的ViewModel
 * @author hxw
 * @date 2019/2/13
 */
class CommonViewModel : ViewModel(), KoinComponent {
    private val api: WanApi by inject()
    fun collectArticle(id: Int, action: () -> Unit) {
        viewModelScope.launch(Dispatchers.Main + exceptionHandler) {
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
        viewModelScope.launch(Dispatchers.Main + exceptionHandler) {
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