package com.hxw.wanandroid.mvp.home

import com.hxw.core.autodispose.AutoDisposeViewModel
import com.hxw.core.utils.AppUtils
import com.hxw.wanandroid.Constant
import com.hxw.wanandroid.WanApi
import com.hxw.wanandroid.entity.BannerEntity
import com.uber.autodispose.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * @author hxw
 * @date 2019/1/25
 */
class HomeViewModel(private val wanApi: WanApi):AutoDisposeViewModel() {

    private val bannerData= mutableListOf<BannerEntity>()
    val bannerAdapter by lazy { BannerAdapter(bannerData) }

    fun getBanner(){
        wanApi.banner
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(this@HomeViewModel)
                .subscribe {
                    if (it.errorCode==Constant.NET_SUCCESS){
                        bannerData.addAll(it.data)
                        bannerAdapter.notifyDataSetChanged()
                    }else{
                        AppUtils.showToast(it.errorMsg)
                    }
                }
    }
}