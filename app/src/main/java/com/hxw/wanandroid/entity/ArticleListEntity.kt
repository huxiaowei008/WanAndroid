package com.hxw.wanandroid.entity

data class ArticleListEntity<T>(
        val curPage: Int,
        val datas: MutableList<T>,
        val offset: Int,
        val over: Boolean,
        val pageCount: Int,
        val size: Int,
        val total: Int
)