package com.hxw.wanandroid.entity

data class NaviEntity(
        val articles: MutableList<NaviArticle>,
        val cid: Int,
        val name: String
)