package com.hxw.wanandroid.entity



data class UserEntity(
    val collectIds: MutableList<Int>,
    val email: String,
    val icon: String,
    val id: Int,
    val password: String,
    val type: Int,
    val username: String
)