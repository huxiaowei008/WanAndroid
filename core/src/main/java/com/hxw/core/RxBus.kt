package com.hxw.core

import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable

/**
 * RxBus
 * 不推荐使用,推荐参照,在具体的地方具体使用和订阅
 * @author hxw on 2018/5/3.
 */
@Deprecated("不推荐使用")
object RxBus {
    private val mBus: Relay<Any> by lazy { PublishRelay.create<Any>().toSerialized() }

    /**
     * 发送数据
     * @param o 数据
     */
    fun send(o: Any) {
        mBus.accept(o)
    }

    /**
     * 判断是否被订阅
     *
     * @return `true`: 被订阅 `false`: 没有被订阅
     */
    fun hasObservers(): Boolean {
        return mBus.hasObservers()
    }

    /**
     * 过滤被观察的数据为指定的类型
     *
     * @param eventType 指定的类型
     */
    fun <T> asObservable(eventType: Class<T>): Observable<T> {
        return mBus.ofType(eventType)
    }
}
