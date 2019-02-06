package com.hxw.wanandroid.entity

/**
 * @author hxw
 * @date 2018/6/20
 */
data class BaseEntity<T>(
        val data: T,
        val errorCode: Int,
        val errorMsg: String
) {}

data class BaseListEntity<T>(
        val data: MutableList<T>,
        val errorCode: Int,
        val errorMsg: String
){}
