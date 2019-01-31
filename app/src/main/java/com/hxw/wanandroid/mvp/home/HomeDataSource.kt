package com.hxw.wanandroid.mvp.home

import android.annotation.SuppressLint
import androidx.paging.PageKeyedDataSource
import com.hxw.wanandroid.Constant
import com.hxw.wanandroid.WanApi
import com.hxw.wanandroid.entity.ArticleEntity
import com.hxw.wanandroid.paging.BasePageDataSource
import com.hxw.wanandroid.paging.NetworkState
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber

/**
 * @author hxw
 * @date 2019/1/30
 */
class HomeDataSource(private val wanApi: WanApi) : BasePageDataSource<Int, ArticleEntity>() {

    @SuppressLint("CheckResult")
    override fun loadInitial(params: PageKeyedDataSource.LoadInitialParams<Int>, callback: PageKeyedDataSource.LoadInitialCallback<Int, ArticleEntity>) {
        Timber.i("loadInitial-> ")
        refreshState.postValue(NetworkState.LOADING)

        wanApi.getHomeArticle(0)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {
                    refreshState.postValue(NetworkState.error(it.message ?: "unknown err"))
                    retry = { loadInitial(params, callback) }
                }
                .subscribe {
                    if (it.errorCode == Constant.NET_SUCCESS) {
                        refreshState.postValue(NetworkState.SUCCESS)
                        retry = null
                        callback.onResult(it.data.datas, 0, it.data.total, null, 1)
                    } else {
                        refreshState.postValue(NetworkState.error(it.errorMsg))
                        retry = { loadInitial(params, callback) }
                    }
                }
    }

    @SuppressLint("CheckResult")
    override fun loadBefore(params: PageKeyedDataSource.LoadParams<Int>, callback: PageKeyedDataSource.LoadCallback<Int, ArticleEntity>) {
        Timber.i("loadBefore->${params.key} ")
        if (params.key < 0) {
            return
        }
        networkState.postValue(NetworkState.LOADING)
        wanApi.getHomeArticle(params.key)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {
                    networkState.postValue(NetworkState.error(it.message ?: "unknown err"))
                    retry = { loadBefore(params, callback) }
                }
                .subscribe {
                    if (it.errorCode == Constant.NET_SUCCESS) {
                        networkState.postValue(NetworkState.SUCCESS)
                        retry = null
                        callback.onResult(it.data.datas, params.key - 1)
                    } else {
                        networkState.postValue(NetworkState.error(it.errorMsg))
                        retry = { loadBefore(params, callback) }
                    }
                }
    }

    @SuppressLint("CheckResult")
    override fun loadAfter(params: PageKeyedDataSource.LoadParams<Int>, callback: PageKeyedDataSource.LoadCallback<Int, ArticleEntity>) {
        Timber.i("loadAfter->${params.key}")
        networkState.postValue(NetworkState.LOADING)
        wanApi.getHomeArticle(params.key)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {
                    networkState.postValue(NetworkState.error(it.message ?: "unknown err"))
                    retry = { loadAfter(params, callback) }
                }
                .subscribe {
                    if (it.errorCode == Constant.NET_SUCCESS) {
                        networkState.postValue(NetworkState.SUCCESS)
                        retry = null
                        callback.onResult(it.data.datas, params.key + 1)
                    } else {
                        networkState.postValue(NetworkState.error(it.errorMsg))
                        retry = { loadBefore(params, callback) }
                    }
                }
    }
}


