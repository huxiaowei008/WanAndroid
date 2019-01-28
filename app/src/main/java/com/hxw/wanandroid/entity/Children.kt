package com.hxw.wanandroid.entity

/**
 * @author hxw
 * @date 2018/6/20
 */
data class Children(
        val children: MutableList<Any>,
        val courseId: Int,
        val id: Int,
        val name: String,
        val order: Int,
        val parentChapterId: Int,
        val visible: Int
)