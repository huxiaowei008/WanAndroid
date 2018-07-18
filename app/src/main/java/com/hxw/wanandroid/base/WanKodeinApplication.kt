package com.hxw.wanandroid.base

import com.hxw.core.base.AbstractApplication
import com.hxw.core.base.coreModule
import com.hxw.wanandroid.BuildConfig
import com.hxw.wanandroid.WanApi
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import timber.log.Timber

/**
 * @author hxw on 2018/7/17.
 */
class WanKodeinApplication : AbstractApplication() {
    override val kodein: Kodein = Kodein.lazy {
        import(coreModule(this@WanKodeinApplication, GlobalConfigModule()))

        bind<WanApi>() with singleton { instance<Retrofit>().create(WanApi::class.java) }
    }


    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}
