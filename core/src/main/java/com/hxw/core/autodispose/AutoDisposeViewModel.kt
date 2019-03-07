package com.hxw.core.autodispose

import androidx.lifecycle.ViewModel
import com.hxw.core.utils.onError
import com.uber.autodispose.lifecycle.CorrespondingEventsFunction
import com.uber.autodispose.lifecycle.LifecycleEndedException
import com.uber.autodispose.lifecycle.LifecycleScopeProvider
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.*
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

/**
 * 自带rx订阅解除的ViewModel
 * @author hxw
 * @date 2018/12/17
 */
abstract class AutoDisposeViewModel : ViewModel(),
    LifecycleScopeProvider<AutoDisposeViewModel.ViewModelEvent>, CoroutineScope {
    companion object {
        /**
         * Function of current event -> target disposal event. ViewModel has a very simple lifecycle.
         * It is created and then later on cleared. So we only have two events and all subscriptions
         * will only be disposed at [ViewModelEvent.CLEARED].
         */
        private val CORRESPONDING_EVENTS = CorrespondingEventsFunction<ViewModelEvent> { event ->
            when (event) {
                ViewModelEvent.CREATED -> ViewModelEvent.CLEARED
                else -> throw LifecycleEndedException("Cannot bind to ViewModel lifecycle after onCleared.")
            }
        }
    }

    private val job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    // Subject backing the auto disposing of subscriptions.
    private val lifecycleEvents = BehaviorSubject.createDefault(ViewModelEvent.CREATED)
    private val mCompositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    /**
     * The observable representing the lifecycle of the [ViewModel].
     *
     * @return [Observable] modelling the [ViewModel] lifecycle.
     */
    override fun lifecycle(): Observable<ViewModelEvent> {
        return lifecycleEvents.hide()
    }

    /**
     * Returns a [CorrespondingEventsFunction] that maps the
     * current event -> target disposal event.
     *
     * @return function mapping the current event to terminal event.
     */
    override fun correspondingEvents(): CorrespondingEventsFunction<ViewModelEvent> {
        return CORRESPONDING_EVENTS
    }

    override fun peekLifecycle(): ViewModelEvent? {
        return lifecycleEvents.value
    }

    /**
     * Emit the [ViewModelEvent.CLEARED] event to
     * dispose off any subscriptions in the ViewModel.
     */
    override fun onCleared() {
        lifecycleEvents.onNext(ViewModelEvent.CLEARED)
        super.onCleared()
        mCompositeDisposable.clear()
        job.cancel()
    }

    /**
     * 自带的解除RX订阅的方式
     *
     * @param disposable 可以解除订阅的对象
     */
    protected fun addDispose(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }

    /**
     * The events that represent the lifecycle of a [ViewModel].
     *
     * The [ViewModel] lifecycle is very simple. It is created
     * and then allows you to clean up any resources in the
     * [ViewModel.onCleared] method before it is destroyed.
     */
    enum class ViewModelEvent {
        CREATED, CLEARED
    }

    fun <T> Deferred<T>.subscribe(
        success: (result: T) -> Unit,
        error: (t: Throwable) -> Unit = { it.onError() }
    ) {
        launch {
            try {
                val result = this@subscribe.await()
                success.invoke(result)
            } catch (t: Throwable) {
                error.invoke(t)
            }
        }
    }
}