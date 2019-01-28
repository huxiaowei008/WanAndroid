package com.hxw.wanandroid.entity

/**
 * @author hxw
 * @date 2018/6/20
 */
data class BannerEntity(
        val desc: String,
        val id: Int,
        val imagePath: String,
        val isVisible: Int,
        val order: Int,
        val title: String,
        val type: Int,
        val url: String
)