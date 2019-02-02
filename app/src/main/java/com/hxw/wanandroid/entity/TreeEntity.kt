package com.hxw.wanandroid.entity

import java.io.Serializable

/**
 * @author hxw
 * @date 2018/6/20
 */
data class TreeEntity(
        val children: MutableList<TreeEntity>,
        val courseId: Int,
        val id: Int,
        val name: String,
        val order: Int,
        val parentChapterId: Int,
        val userControlSetTop:Boolean,
        val visible: Int
):Serializable