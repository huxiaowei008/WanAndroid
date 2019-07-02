package com.hxw.core.autodispose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hxw.core.base.exceptionHandler
import com.hxw.core.utils.onError
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * 自带rx订阅解除的ViewModel
 * @author hxw
 * @date 2018/12/17
 */
abstract class AutoDisposeViewModel : ViewModel() {


    /**
     * Emit the [ViewModelEvent.CLEARED] event to
     * dispose off any subscriptions in the ViewModel.
     */
    override fun onCleared() {
        super.onCleared()
    }
}