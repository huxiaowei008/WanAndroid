package com.hxw.wanandroid.entity

/**
 * @author hxw
 * @date 2018/6/20
 */
data class NaviEntity(
        val articles: MutableList<ArticleEntity>,
        val cid: Int,
        val name: String
)