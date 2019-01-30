package com.hxw.wanandroid.mvp.home

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.hxw.core.utils.AppUtils
import com.hxw.core.utils.constant.NetworkState
import com.hxw.wanandroid.Constant
import com.hxw.wanandroid.WanApi
import com.hxw.wanandroid.entity.ArticleEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber

/**
 * @author hxw
 * @date 2019/1/30
 */
class HomeDataSource(private val wanApi: WanApi) : PageKeyedDataSource<Int, ArticleEntity>() {

    val networkState = MutableLiveData<NetworkState>()

    override fun loadInitial(params: PageKeyedDataSource.LoadInitialParams<Int>, callback: PageKeyedDataSource.LoadInitialCallback<Int, ArticleEntity>) {
        Timber.i("loadInitial-> ")
        val d = wanApi.getHomeArticle(0)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { networkState.postValue(NetworkState.LOADING) }
                .doOnError {
                    networkState.postValue(NetworkState.error(it.message ?: "unknown err"))
                }
                .subscribe {
                    if (it.errorCode == Constant.NET_SUCCESS) {
                        networkState.postValue(NetworkState.SUCCESS)
                        callback.onResult(it.data.datas, 0, it.data.total, null, 1)
                    } else {
                        networkState.postValue(NetworkState.error(it.errorMsg))
                    }
                }
    }

    override fun loadBefore(params: PageKeyedDataSource.LoadParams<Int>, callback: PageKeyedDataSource.LoadCallback<Int, ArticleEntity>) {
        Timber.i("loadBefore->${params.key} ")
        if (params.key < 0) {
            return
        }
        val d = wanApi.getHomeArticle(params.key)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it.errorCode == Constant.NET_SUCCESS) {
                        callback.onResult(it.data.datas, params.key - 1)
                    } else {
                        AppUtils.showToast(it.errorMsg)
                    }
                }
    }

    override fun loadAfter(params: PageKeyedDataSource.LoadParams<Int>, callback: PageKeyedDataSource.LoadCallback<Int, ArticleEntity>) {
        Timber.i("loadAfter->${params.key}")
        val d = wanApi.getHomeArticle(params.key)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it.errorCode == Constant.NET_SUCCESS) {
                        callback.onResult(it.data.datas, params.key + 1)
                    } else {
                        AppUtils.showToast(it.errorMsg)
                    }
                }
    }
}

class HomeDataSourceFactory(private val wanApi: WanApi) : DataSource.Factory<Int, ArticleEntity>() {
    val sourceLiveData = MutableLiveData<HomeDataSource>()
    override fun create(): DataSource<Int, ArticleEntity> {
        val source = HomeDataSource(wanApi)
        sourceLiveData.postValue(source)
        return source
    }

}


