package com.hxw.wanandroid.entity

data class CollectListEntity(
        val curPage: Int,
        val datas: List<CollectData>,
        val offset: Int,
        val over: Boolean,
        val pageCount: Int,
        val size: Int,
        val total: Int
)