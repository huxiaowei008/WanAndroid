package com.hxw.wanandroid.mvp.home

import com.hxw.wanandroid.Constant
import com.hxw.wanandroid.WanApi
import com.hxw.wanandroid.entity.ArticleEntity
import com.hxw.wanandroid.paging.BasePageDataSource
import com.hxw.wanandroid.paging.NetworkState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.await

/**
 * @author hxw
 * @date 2019/1/30
 */
class HomeDataSource(private val wanApi: WanApi, private val scope: CoroutineScope) :
    BasePageDataSource<Int, ArticleEntity>() {


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, ArticleEntity>
    ) {
        refreshState.postValue(NetworkState.LOADING)
        scope.launch(CoroutineExceptionHandler { _, throwable ->
            refreshState.postValue(NetworkState.error(throwable.message ?: "unknown err"))
            retry = { loadInitial(params, callback) }
        }) {
            val result = wanApi.getHomeArticle(0).await()
            if (result.errorCode == Constant.NET_SUCCESS) {
                refreshState.postValue(NetworkState.SUCCESS)
                retry = null
                callback.onResult(result.data.datas, 0, result.data.total, null, 1)
            } else {
                refreshState.postValue(NetworkState.error(result.errorMsg))
                retry = { loadInitial(params, callback) }
            }
        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, ArticleEntity>
    ) {
        if (params.key < 0) {
            return
        }
        networkState.postValue(NetworkState.LOADING)
        scope.launch(CoroutineExceptionHandler { _, throwable ->
            networkState.postValue(NetworkState.error(throwable.message ?: "unknown err"))
            retry = { loadBefore(params, callback) }
        }) {
            val result = wanApi.getHomeArticle(params.key).await()
            if (result.errorCode == Constant.NET_SUCCESS) {
                networkState.postValue(NetworkState.SUCCESS)
                retry = null
                callback.onResult(result.data.datas, params.key - 1)
            } else {
                networkState.postValue(NetworkState.error(result.errorMsg))
                retry = { loadBefore(params, callback) }
            }
        }
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, ArticleEntity>
    ) {
        networkState.postValue(NetworkState.LOADING)
        scope.launch(CoroutineExceptionHandler { _, throwable ->
            networkState.postValue(NetworkState.error(throwable.message ?: "unknown err"))
            retry = { loadAfter(params, callback) }
        }) {
            val result = wanApi.getHomeArticle(params.key).await()
            if (result.errorCode == Constant.NET_SUCCESS) {
                networkState.postValue(NetworkState.SUCCESS)
                retry = null
                callback.onResult(result.data.datas, params.key + 1)
            } else {
                networkState.postValue(NetworkState.error(result.errorMsg))
                retry = { loadBefore(params, callback) }
            }
        }
    }
}


