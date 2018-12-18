package com.hxw.core.autodispose

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import com.uber.autodispose.lifecycle.CorrespondingEventsFunction
import com.uber.autodispose.lifecycle.LifecycleEndedException
import com.uber.autodispose.lifecycle.LifecycleScopeProvider
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

/**
 * @author hxw on 2018/12/17
 */
abstract class AutoDisposeViewKotlin : View, LifecycleScopeProvider<AutoDisposeViewKotlin.ViewEvent> {

    companion object {

        /**
         * This is a function of current event -> target disposal event. That is to say that if event
         * "Attach" returns "Detach", then any stream subscribed to during Attach will autodispose on
         * Detach.
         */
        private val CORRESPONDING_EVENTS = CorrespondingEventsFunction<ViewEvent> { viewEvent ->
            when (viewEvent) {
                ViewEvent.ATTACH -> ViewEvent.DETACH
                else -> throw LifecycleEndedException(
                        "Cannot bind to View lifecycle after detach.")
            }
        }
    }

    private val lifecycleEvents by lazy { BehaviorSubject.create<ViewEvent>() }

    @JvmOverloads
    constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = View.NO_ID)
            : super(context, attrs, defStyleAttr)

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        lifecycleEvents.onNext(ViewEvent.ATTACH)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        lifecycleEvents.onNext(ViewEvent.DETACH)
    }

    override fun lifecycle(): Observable<ViewEvent> = lifecycleEvents.hide()

    override fun correspondingEvents(): CorrespondingEventsFunction<ViewEvent> {
        return CORRESPONDING_EVENTS
    }

    override fun peekLifecycle(): ViewEvent? {
        return lifecycleEvents.value
    }

    enum class ViewEvent {
        ATTACH, DETACH
    }

}