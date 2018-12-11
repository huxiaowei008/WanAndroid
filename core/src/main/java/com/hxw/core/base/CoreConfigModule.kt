package com.hxw.core.base

import android.app.Application
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hxw.core.utils.AppUtils
import com.hxw.core.utils.DateUtils
import com.hxw.core.utils.EncryptUtils
import com.hxw.core.utils.StringUtils
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.uber.autodispose.*
import com.uber.autodispose.android.lifecycle.scope
import io.reactivex.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.parallel.ParallelFlowable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein
import org.kodein.di.android.androidModule
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import org.kodein.di.jxinject.jxInjectorModule
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.*

/**
 * 框架核心Module提供,和一些kotlin的拓展函数
 * @author hxw on 2018/7/18.
 *
 */
fun coreModule(app: Application, configModule: ConfigModule) = Kodein.Module("MyCoreConfig") {
    importOnce(androidModule(app))
    importOnce(androidXModule(app))
    importOnce(jxInjectorModule)

    bind() from provider { configModule }

    bind<Gson>() with singleton {
        val builder = GsonBuilder()
                .serializeNulls()
        configModule.configGson(app, builder)
        builder.create()
    }

    bind<OkHttpClient>() with singleton {
        val logging = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            var str = it
            if (it.startsWith("{") || it.startsWith("[")) {
                str = it.jsonFormat()
            }
            Timber.tag("OkHttp").i(str)
        })

        logging.level = HttpLoggingInterceptor.Level.BODY
        val builder = OkHttpClient.Builder()
                .addInterceptor(logging)
        configModule.configOkHttp(app, builder)
        builder.build()
    }

    bind<Retrofit>() with singleton {
        val builder = Retrofit.Builder()
                .client(instance())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create(instance()))
        configModule.configRetrofit(app, builder)
        builder.build()
    }
}

fun String.jsonFormat() = StringUtils.jsonFormat(this)!!

fun Float.dpToPx(context: Context) = AppUtils.dpToPx(context, this)

fun Float.spToPx(context: Context) = AppUtils.spToPx(context, this)

fun Date.date2String(pattern: String = "yyyy-MM-dd HH:mm:ss") = DateUtils.date2String(this, pattern)

fun String.string2Date(pattern: String = "yyyy-MM-dd HH:mm:ss") = DateUtils.string2Date(this, pattern)

fun String?.encryptMD5ToString() = EncryptUtils.encryptMD5ToString(this)

fun String.encryptMD5() = EncryptUtils.encryptMD5(this.toByteArray())

fun String?.encryptSHA1ToString() = EncryptUtils.encryptSHA1ToString(this)

fun String.encryptSHA1() = EncryptUtils.encryptSHA1(this.toByteArray())

/**
 * Extension that proxies to [Flowable. as] + [AutoDispose.autoDisposable]
 */
@CheckReturnValue
fun <T> Flowable<T>.autoDisposable(lifecycleOwner: LifecycleOwner,
                                          untilEvent: Lifecycle.Event = Lifecycle.Event.ON_STOP): FlowableSubscribeProxy<T> = this.autoDisposable(lifecycleOwner.scope(untilEvent))

/**
 * Extension that proxies to [Observable. as] + [AutoDispose.autoDisposable]
 */
@CheckReturnValue
fun <T> Observable<T>.autoDisposable(lifecycleOwner: LifecycleOwner,
                                            untilEvent: Lifecycle.Event = Lifecycle.Event.ON_STOP): ObservableSubscribeProxy<T> = this.autoDisposable(lifecycleOwner.scope(untilEvent))

/**
 * Extension that proxies to [Single. as] + [AutoDispose.autoDisposable]
 */
@CheckReturnValue
fun <T> Single<T>.autoDisposable(lifecycleOwner: LifecycleOwner,
                                        untilEvent: Lifecycle.Event = Lifecycle.Event.ON_STOP): SingleSubscribeProxy<T> = this.autoDisposable(lifecycleOwner.scope(untilEvent))

/**
 * Extension that proxies to [Maybe. as] + [AutoDispose.autoDisposable]
 */
@CheckReturnValue
fun <T> Maybe<T>.autoDisposable(lifecycleOwner: LifecycleOwner,
                                       untilEvent: Lifecycle.Event = Lifecycle.Event.ON_STOP): MaybeSubscribeProxy<T> = this.autoDisposable(lifecycleOwner.scope(untilEvent))

/**
 * Extension that proxies to [Completable. as] + [AutoDispose.autoDisposable]
 */
@CheckReturnValue
fun Completable.autoDisposable(lifecycleOwner: LifecycleOwner,
                                      untilEvent: Lifecycle.Event = Lifecycle.Event.ON_STOP): CompletableSubscribeProxy = this.autoDisposable(lifecycleOwner.scope(untilEvent))

/**
 * Extension that proxies to [ParallelFlowable. as] + [AutoDispose.autoDisposable]
 */
@CheckReturnValue
fun <T> ParallelFlowable<T>.autoDisposable(lifecycleOwner: LifecycleOwner,
                                                  untilEvent: Lifecycle.Event = Lifecycle.Event.ON_STOP): ParallelFlowableSubscribeProxy<T> = this.autoDisposable(lifecycleOwner.scope(untilEvent))