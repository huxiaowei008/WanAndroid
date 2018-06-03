package com.hxw.core;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import io.reactivex.Observable;

/**
 * RxBus
 *
 * @author hxw on 2018/5/3.
 */
public class RxBus {
    private static volatile RxBus INSTANCE;
    private final Relay<Object> mBus;

    private RxBus() {
        mBus = PublishRelay.create().toSerialized();
    }

    public static RxBus getInstance() {
        if (INSTANCE == null) {
            synchronized (RxBus.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RxBus();
                }
            }
        }
        return INSTANCE;
    }

    public void send(Object o) {
        mBus.accept(o);
    }

    public boolean hasObservers() {
        return mBus.hasObservers();
    }

    public <T> Observable<T> asObservable(Class<T> eventType) {
        return mBus.ofType(eventType);
    }
}
