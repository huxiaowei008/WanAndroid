package com.hxw.core.autodispose

import androidx.lifecycle.ViewModel
import com.hxw.core.utils.onError
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * 自带rx订阅解除的ViewModel
 * @author hxw
 * @date 2018/12/17
 */
abstract class AutoDisposeViewModel : ViewModel(), CoroutineScope {

    private val job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    /**
     * Emit the [ViewModelEvent.CLEARED] event to
     * dispose off any subscriptions in the ViewModel.
     */
    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}

fun <T> Deferred<T>.subscribe(
    scope: CoroutineScope,
    success: (result: T) -> Unit,
    error: (t: Throwable) -> Unit = { it.onError() },
    complete: () -> Unit = {}
) {
    scope.launch {
        try {
            val result = this@subscribe.await()
            success.invoke(result)
        } catch (t: Throwable) {
            error.invoke(t)
        } finally {
            complete.invoke()
        }
    }
}