package com.hxw.wanandroid.mvp

import com.hxw.core.autodispose.AutoDisposeViewModel
import com.hxw.core.utils.AppUtils
import com.hxw.wanandroid.Constant
import com.hxw.wanandroid.WanApi
import com.uber.autodispose.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * 通用操作的ViewModel
 * @author hxw
 * @date 2019/2/13
 */
class CommonViewModel(private val api: WanApi) : AutoDisposeViewModel() {


    fun collectArticle(id: Int) {
        api.collect(id)
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(this)
                .subscribe {
                    if (it.errorCode == Constant.NET_SUCCESS) {
                        AppUtils.showToast("收藏成功")
                    } else {
                        AppUtils.showToast(it.errorMsg)
                    }
                }
    }

    fun unCollectArticle(id: Int){
        api.unCollect(id)
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(this)
                .subscribe {
                    if (it.errorCode == Constant.NET_SUCCESS) {
                        AppUtils.showToast("取消收藏")
                    } else {
                        AppUtils.showToast(it.errorMsg)
                    }
                }
    }

}