package com.hxw.wanandroid.mvp.project

import androidx.paging.PageKeyedDataSource
import com.hxw.core.base.subscribe
import com.hxw.wanandroid.Constant
import com.hxw.wanandroid.WanApi
import com.hxw.wanandroid.entity.ArticleEntity
import com.hxw.wanandroid.paging.BasePageDataSource
import com.hxw.wanandroid.paging.NetworkState
import kotlinx.coroutines.CoroutineScope
import timber.log.Timber

/**
 * @author hxw
 * @date 2019/1/30
 */
class ProjectMoreDataSource(
    private val wanApi: WanApi,
    private val cid: Int,
    private val scope: CoroutineScope
) :
    BasePageDataSource<Int, ArticleEntity>() {

    override fun loadInitial(
        params: PageKeyedDataSource.LoadInitialParams<Int>,
        callback: PageKeyedDataSource.LoadInitialCallback<Int, ArticleEntity>
    ) {
        Timber.i("loadInitial-> ")
        refreshState.postValue(NetworkState.LOADING)
        wanApi.getProjectList(1, cid)
            .subscribe(scope, {
                if (it.errorCode == Constant.NET_SUCCESS) {
                    refreshState.postValue(NetworkState.SUCCESS)
                    retry = null
                    callback.onResult(it.data.datas, 0, it.data.total, null, 2)
                } else {
                    refreshState.postValue(NetworkState.error(it.errorMsg))
                    retry = { loadInitial(params, callback) }
                }
            }, {
                refreshState.postValue(NetworkState.error(it.message ?: "unknown err"))
                retry = { loadInitial(params, callback) }
            })
    }

    override fun loadBefore(
        params: PageKeyedDataSource.LoadParams<Int>,
        callback: PageKeyedDataSource.LoadCallback<Int, ArticleEntity>
    ) {
        Timber.i("loadBefore->${params.key} ")
        if (params.key < 1) {
            return
        }
        networkState.postValue(NetworkState.LOADING)
        wanApi.getProjectList(params.key, cid)
            .subscribe(scope, {
                if (it.errorCode == Constant.NET_SUCCESS) {
                    networkState.postValue(NetworkState.SUCCESS)
                    retry = null
                    callback.onResult(it.data.datas, params.key - 1)
                } else {
                    networkState.postValue(NetworkState.error(it.errorMsg))
                    retry = { loadBefore(params, callback) }
                }
            }, {
                networkState.postValue(NetworkState.error(it.message ?: "unknown err"))
                retry = { loadBefore(params, callback) }
            })
    }

    override fun loadAfter(
        params: PageKeyedDataSource.LoadParams<Int>,
        callback: PageKeyedDataSource.LoadCallback<Int, ArticleEntity>
    ) {
        Timber.i("loadAfter->${params.key}")
        networkState.postValue(NetworkState.LOADING)
        wanApi.getProjectList(params.key, cid)
            .subscribe(scope, {
                if (it.errorCode == Constant.NET_SUCCESS) {
                    networkState.postValue(NetworkState.SUCCESS)
                    retry = null
                    callback.onResult(it.data.datas, params.key + 1)
                } else {
                    networkState.postValue(NetworkState.error(it.errorMsg))
                    retry = { loadBefore(params, callback) }
                }
            }, {
                networkState.postValue(NetworkState.error(it.message ?: "unknown err"))
                retry = { loadAfter(params, callback) }
            })
    }
}


