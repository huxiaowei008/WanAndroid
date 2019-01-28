package com.hxw.wanandroid.entity

/**
 * @author hxw
 * @date 2018/6/20
 */
data class CollectListEntity(
        val curPage: Int,
        val datas: List<CollectData>,
        val offset: Int,
        val over: Boolean,
        val pageCount: Int,
        val size: Int,
        val total: Int
)