package com.hxw.wanandroid.base

import com.hxw.core.base.ConfigModule
import com.hxw.wanandroid.WanApi
import com.hxw.wanandroid.mvp.CommonViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create

/**
 * @author hxw
 * @date 2019/1/24
 */
val appModule = module {

    single<ConfigModule> { GlobalConfigModule() }

    single { get<Retrofit>().create<WanApi>() }

    viewModel { CommonViewModel(get()) }
}