package com.hxw.wanandroid.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author hxw
 * @date 2019/1/31
 */
abstract class BasePageDataSource<Key, Value> : PageKeyedDataSource<Key, Value>() {
    //keep a function reference for the retry event
    protected var retry: (() -> Any)? = null

    /**
     * There is no sync on the state because paging will always call loadInitial first
     * then wait for it to return some success value before calling loadAfter.
     */
    val networkState = MutableLiveData<NetworkState>()

    val refreshState = MutableLiveData<NetworkState>()

    /**
     * 重新加载
     */
    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            GlobalScope.launch(Dispatchers.IO) {
                it.invoke()
            }
        }
    }
}