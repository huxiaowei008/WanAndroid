package com.hxw.wanandroid.mvp.wxarticle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.paging.toLiveData
import androidx.recyclerview.widget.DiffUtil
import com.hxw.core.base.exceptionMain
import com.hxw.core.utils.showToast
import com.hxw.wanandroid.Constant
import com.hxw.wanandroid.R
import com.hxw.wanandroid.WanApi
import com.hxw.wanandroid.entity.ArticleEntity
import com.hxw.wanandroid.entity.TreeEntity
import com.hxw.wanandroid.paging.BasePageViewModel
import com.hxw.wanandroid.paging.PageSourceFactory
import com.hxw.wanandroid.paging.SimplePagedListAdapter
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * @author hxw
 * @date 2019/2/5
 */
class WXArticleViewModel : BasePageViewModel<Int, ArticleEntity>(), KoinComponent {
    private var id: Int = 0
    private var key: String = ""
    private val wanApi: WanApi by inject()
    val treeData = MutableLiveData<MutableList<TreeEntity>>()
    override val sourceFactory: PageSourceFactory<Int, ArticleEntity> = PageSourceFactory {
        WXArticleDataSource(wanApi, id, key, viewModelScope)
    }

    override val pagedList: LiveData<PagedList<ArticleEntity>> = sourceFactory
        .toLiveData(20)

    val wxAdapter: SimplePagedListAdapter<ArticleEntity> by lazy {
        SimplePagedListAdapter(
            R.layout.item_article,
            object : DiffUtil.ItemCallback<ArticleEntity>() {
                override fun areItemsTheSame(
                    oldItem: ArticleEntity,
                    newItem: ArticleEntity
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: ArticleEntity,
                    newItem: ArticleEntity
                ): Boolean {
                    return oldItem == newItem
                }
            })
    }

    fun changeId(id: Int) {
        this.id = id
        refresh()
    }

    fun changeKey(key: String) {
        this.key = key
        refresh()
    }

    fun getWxPublish() {
        viewModelScope.launch(exceptionMain) {
            val result = wanApi.getWXPublic()
            if (result.errorCode == Constant.NET_SUCCESS) {
                treeData.value = result.data
            } else {
                showToast(result.errorMsg)
            }
        }
    }
}