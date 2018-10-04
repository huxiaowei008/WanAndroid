package com.hxw.core.integration

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 额外代理方式
 * @author hxw on 2017/8/28
 */
object DelegatesExt {
    fun <T> notNullSingleValue() = NotNullSingleValueVar<T>()
}

/**
 * 仅能初始化一次的非空值
 */
class NotNullSingleValueVar<T> : ReadWriteProperty<Any?, T> {

    private var value: T? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): T =
            value ?: throw IllegalStateException("${property.name} not initialized")

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = if (this.value == null) value
        else throw IllegalStateException("${property.name} already initialized")
    }
}