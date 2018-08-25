package com.hxw.wanandroid.entity

data class TreeEntity(
        val children: MutableList<TreeEntity>?,
        val courseId: Int,
        val id: Int,
        val name: String,
        val order: Int,
        val parentChapterId: Int,
        val visible: Int
)