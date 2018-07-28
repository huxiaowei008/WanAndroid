package com.hxw.wanandroid.mvp.home

import com.hxw.core.AbstractErrorSubscriber
import com.hxw.core.mvp.BasePresenter
import com.hxw.core.utils.AppUtils
import com.hxw.wanandroid.WanApi
import com.hxw.wanandroid.entity.ArticleData
import com.hxw.wanandroid.entity.ArticleListEntity
import com.hxw.wanandroid.entity.BaseEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * @author hxw on 2018/7/28
 */
class HomePresenter @Inject constructor(private val api: WanApi) : BasePresenter<HomeView>() {

    fun getHomeArticle(page: Int) {
        api.getHomeArticle(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .`as`(bindLifecycle<BaseEntity<ArticleListEntity<ArticleData>>>())
                .subscribe(object : AbstractErrorSubscriber<BaseEntity<ArticleListEntity<ArticleData>>>() {
                    override fun onNext(t: BaseEntity<ArticleListEntity<ArticleData>>) {
                        if (t.errorCode == 0) {
                            mView.addArticleData(t.data)
                        } else {
                            AppUtils.showToast(t.errorMsg)
                        }
                    }
                })

    }
}