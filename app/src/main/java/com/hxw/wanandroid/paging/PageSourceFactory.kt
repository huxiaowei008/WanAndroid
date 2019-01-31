package com.hxw.wanandroid.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource

/**
 * 用于创建BasePageDataSource的Factory
 * @author hxw
 * @date 2019/1/31
 */
class PageSourceFactory<Key, Value>(private val createSource: () -> BasePageDataSource<Key, Value>) : DataSource.Factory<Key, Value>() {
    val sourceLivaData = MutableLiveData<BasePageDataSource<Key, Value>>()

    override fun create(): DataSource<Key, Value> {
        val source = createSource()
        sourceLivaData.postValue(source)
        return source
    }
}