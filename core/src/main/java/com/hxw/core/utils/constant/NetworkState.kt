package com.hxw.core.utils.constant

/**
 * 网络请求状态
 *
 * @author hxw
 * @date 2019/1/30
 */
data class NetworkState private constructor(
        @NetworkStateConstants.Unit
        val status: Int,
        val msg: String? = null
) {
    companion object {
        val LOADING = NetworkState(NetworkStateConstants.LOADING)
        val SUCCESS = NetworkState(NetworkStateConstants.SUCCESS)
        fun error(msg: String?) = NetworkState(NetworkStateConstants.FAILED, msg)
    }
}