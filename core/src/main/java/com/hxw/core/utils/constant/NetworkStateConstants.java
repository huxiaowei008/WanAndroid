package com.hxw.core.utils.constant;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/**
 * 网络请求状态常量
 *
 * @author hxw
 * @date 2019/1/30
 */
public final class NetworkStateConstants {

    private NetworkStateConstants() {
    }

    /**
     * 加载中
     */
    public static final int LOADING = 0;

    /**
     * 请求成功
     */
    public static final int SUCCESS = 1;

    /**
     * 请求失败
     */
    public static final int FAILED = 2;

    @IntDef({LOADING, SUCCESS, FAILED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Unit {
    }
}
