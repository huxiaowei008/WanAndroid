package com.hxw.wanandroid.base

import com.hxw.core.base.ConfigModule
import com.hxw.wanandroid.WanApi
import org.koin.dsl.module
import retrofit2.Retrofit

/**
 * @author hxw
 * @date 2019/1/24
 */
val appModule = module {

    single<ConfigModule> { GlobalConfigModule() }

    single { get<Retrofit>().create(WanApi::class.java) }
}