package com.hxw.wanandroid.entity


/**
 * @author hxw
 * @date 2018/6/20
 */
data class UserEntity(
    val collectIds: MutableList<Int>,
    val email: String,
    val icon: String,
    val id: Int,
    val password: String,
    val type: Int,
    val username: String
)