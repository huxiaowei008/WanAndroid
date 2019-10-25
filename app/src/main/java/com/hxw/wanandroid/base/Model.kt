package com.hxw.wanandroid.base

import com.hxw.core.base.ConfigModule
import com.hxw.wanandroid.WanApi
import com.hxw.wanandroid.mvp.CommonViewModel
import com.hxw.wanandroid.mvp.home.HomeViewModel
import com.hxw.wanandroid.mvp.login.LoginViewModel
import com.hxw.wanandroid.mvp.project.ProjectMoreViewModel
import com.hxw.wanandroid.mvp.project.ProjectViewModel
import com.hxw.wanandroid.mvp.wxarticle.WXArticleViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
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

val viewModel = module {

    viewModel { LoginViewModel(get()) }

    viewModel { HomeViewModel(get()) }

    viewModel { ProjectViewModel(get()) }

    viewModel { ProjectMoreViewModel(get()) }

    viewModel { WXArticleViewModel(get()) }

    viewModel { CommonViewModel(get()) }
}