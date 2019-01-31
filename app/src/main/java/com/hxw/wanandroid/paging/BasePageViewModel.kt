package com.hxw.wanandroid.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations.switchMap
import androidx.paging.PagedList
import com.hxw.core.autodispose.AutoDisposeViewModel

/**
 * 使用paging的ViewModel基类
 *
 * @author hxw
 * @date 2019/1/31
 */
abstract class BasePageViewModel<Key, Value> : AutoDisposeViewModel() {

    abstract val sourceFactory: PageSourceFactory<Key, Value>

    //要观察的数据
    abstract val pagedList: LiveData<PagedList<Value>>
    //网络加载状态
    val networkState by lazy { switchMap(sourceFactory.sourceLivaData) { it.networkState }!! }
    //刷新状态
    val refreshState by lazy { switchMap(sourceFactory.sourceLivaData) { it.refreshState }!! }

    /**
     * 执行刷新操作
     */
    fun refresh() {
        sourceFactory.sourceLivaData.value?.invalidate()
    }

    /**
     * 执行重试操作
     */
    fun retry() {
        sourceFactory.sourceLivaData.value?.retryAllFailed()
    }

}