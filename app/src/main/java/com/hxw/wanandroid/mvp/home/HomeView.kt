package com.hxw.wanandroid.mvp.home

import com.hxw.core.mvp.IView
import com.hxw.wanandroid.entity.ArticleData
import com.hxw.wanandroid.entity.ArticleListEntity
import com.hxw.wanandroid.entity.BannerListEntity

/**
 * @author hxw on 2018/7/28
 */
interface HomeView:IView {

    fun addArticleData(articleListEntity: ArticleListEntity<ArticleData>)

    fun addBanner(bannerListEntity: BannerListEntity)
}