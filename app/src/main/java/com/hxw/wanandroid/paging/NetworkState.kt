package com.hxw.wanandroid.paging

import com.hxw.core.utils.constant.NetworkStateConstants
import java.util.concurrent.Executors

/**
 * 网络请求状态
 *
 * @author hxw
 * @date 2019/1/30
 */
data class NetworkState constructor(
        @NetworkStateConstants.Unit
        val status: Int,
        val msg: String? = null
) {
    companion object {
        val NETWORK_IO = Executors.newFixedThreadPool(5)!!
        val LOADING = NetworkState(NetworkStateConstants.LOADING)
        val SUCCESS = NetworkState(NetworkStateConstants.SUCCESS)
        fun error(msg: String?) = NetworkState(NetworkStateConstants.FAILED, msg)
    }
}