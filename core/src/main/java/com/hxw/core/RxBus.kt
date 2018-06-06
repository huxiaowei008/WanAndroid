package com.hxw.core

import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay

import io.reactivex.Observable

/**
 * RxBus
 *
 * @author hxw on 2018/5/3.
 */
object RxBus {
    private val mBus: Relay<Any> by lazy { PublishRelay.create<Any>().toSerialized() }

    fun send(o: Any) {
        mBus.accept(o)
    }

    fun hasObservers(): Boolean {
        return mBus.hasObservers()
    }

    fun <T> asObservable(eventType: Class<T>): Observable<T> {
        return mBus.ofType(eventType)
    }
}
