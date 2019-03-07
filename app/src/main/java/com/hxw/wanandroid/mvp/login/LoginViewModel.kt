package com.hxw.wanandroid.mvp.login

import androidx.lifecycle.MutableLiveData
import com.hxw.core.autodispose.AutoDisposeViewModel
import com.hxw.core.utils.AppUtils
import com.hxw.core.utils.onError
import com.hxw.wanandroid.Constant
import com.hxw.wanandroid.WanApi
import com.hxw.wanandroid.entity.UserEntity
import com.uber.autodispose.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

/**
 * @author hxw
 * @date 2019/1/24
 */
class LoginViewModel(private val wanApi: WanApi) : AutoDisposeViewModel() {



    val userInfo = MutableLiveData<UserEntity>()

    fun login(username: String, password: String) {
        wanApi.login(username, password)
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(this@LoginViewModel)
            .subscribe {
                if (it.errorCode == Constant.NET_SUCCESS) {
                    AppUtils.showToast("登陆成功")
                    userInfo.value = it.data
                } else {
                    AppUtils.showToast("登陆失败->" + it.errorMsg)
                }
            }
    }

    fun loginDeferred(username: String, password: String) {
        wanApi.loginDeferred("1_4eac7652-dede-4df9-93df-aa52e9b82e68", "1")
            .subscribe({
                Timber.i("result in thread ${Thread.currentThread().name}")
                if (it.data != null) {
                    AppUtils.showToast("登陆成功")
//                    userInfo.value = user.data
                } else {
                    AppUtils.showToast("登陆失败->")
                }
            },{
                it.onError()
                Timber.i("onError in thread ${Thread.currentThread().name}")
            })
    }

    fun register(username: String, password: String, repassword: String) {
        wanApi.register(username, password, repassword)
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(this@LoginViewModel)
            .subscribe {
                if (it.errorCode == Constant.NET_SUCCESS) {
                    AppUtils.showToast("注册成功")
                    userInfo.value = it.data
                } else {
                    AppUtils.showToast("注册失败->" + it.errorMsg)
                }
            }
    }
}