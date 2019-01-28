package com.hxw.wanandroid.entity

/**
 * @author hxw
 * @date 2018/6/20
 */
data class WXPublicEntity(
        val children: List<Any>,
        val courseId: Int,
        val id: Int,
        val name: String,
        val order: Int,
        val parentChapterId: Int,
        val userControlSetTop: Boolean,
        val visible: Int
)