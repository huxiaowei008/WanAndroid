package com.hxw.wanandroid.mvp.login

import androidx.lifecycle.MutableLiveData
import com.hxw.core.autodispose.AutoDisposeViewModel
import com.hxw.core.autodispose.subscribe
import com.hxw.core.utils.AppUtils
import com.hxw.core.utils.onError
import com.hxw.wanandroid.Constant
import com.hxw.wanandroid.WanApi
import com.hxw.wanandroid.entity.UserEntity
import timber.log.Timber

/**
 * @author hxw
 * @date 2019/1/24
 */
class LoginViewModel(private val wanApi: WanApi) : AutoDisposeViewModel() {


    val userInfo = MutableLiveData<UserEntity>()

    fun login(username: String, password: String) {
        wanApi.login(username, password)
            .subscribe(this, {
                if (it.errorCode == Constant.NET_SUCCESS) {
                    AppUtils.showToast("登陆成功")
                    userInfo.value = it.data
                } else {
                    AppUtils.showToast("登陆失败->" + it.errorMsg)
                }
            })
    }

    fun register(username: String, password: String, repassword: String) {
        wanApi.register(username, password, repassword)
            .subscribe(this, {
                if (it.errorCode == Constant.NET_SUCCESS) {
                    AppUtils.showToast("注册成功")
                    userInfo.value = it.data
                } else {
                    AppUtils.showToast("注册失败->" + it.errorMsg)
                }
            })
    }
}