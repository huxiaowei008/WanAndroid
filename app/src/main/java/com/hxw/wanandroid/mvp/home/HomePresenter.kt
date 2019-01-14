package com.hxw.wanandroid.mvp.home

import com.hxw.core.mvp.BasePresenter
import com.hxw.core.utils.AppUtils
import com.hxw.core.utils.onError
import com.hxw.wanandroid.Constant
import com.hxw.wanandroid.WanApi
import com.hxw.wanandroid.entity.ArticleData
import com.hxw.wanandroid.entity.ArticleListEntity
import com.hxw.wanandroid.entity.BannerListEntity
import com.hxw.wanandroid.entity.BaseEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author hxw on 2018/7/28
 */
class HomePresenter constructor(private val api: WanApi) : BasePresenter<HomeView>() {

    fun getHomeArticle(page: Int) {
        api.getHomeArticle(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .`as`(bindLifecycle<BaseEntity<ArticleListEntity<ArticleData>>>())
                .subscribe({
                    if (it.errorCode == Constant.NET_SUCCESS) {
                        mView?.addArticleData(it.data)
                    } else {
                        AppUtils.showToast(it.errorMsg)
                    }
                }, { it.onError() })

    }

    fun getBanner() {
        api.banner
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .`as`(bindLifecycle<BannerListEntity>())
                .subscribe({
                    if (it.errorCode == Constant.NET_SUCCESS) {
                        mView?.addBanner(it)
                    } else {
                        AppUtils.showToast(it.errorMsg)
                    }
                }, { AppUtils.onError(it) })
    }
}