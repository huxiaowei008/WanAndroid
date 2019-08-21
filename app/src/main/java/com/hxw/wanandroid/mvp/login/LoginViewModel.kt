package com.hxw.wanandroid.mvp.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hxw.core.base.subscribe
import com.hxw.core.utils.showToast
import com.hxw.wanandroid.Constant
import com.hxw.wanandroid.WanApi
import com.hxw.wanandroid.entity.UserEntity

/**
 * @author hxw
 * @date 2019/1/24
 */
class LoginViewModel(private val wanApi: WanApi) : ViewModel() {


    val userInfo = MutableLiveData<UserEntity>()

    fun login(username: String, password: String) {
        wanApi.login(username, password)
            .subscribe(viewModelScope, {
                if (it.errorCode == Constant.NET_SUCCESS) {
                    showToast("登陆成功")
                    userInfo.value = it.data
                } else {
                    showToast("登陆失败->" + it.errorMsg)
                }
            })
    }

    fun register(username: String, password: String, repassword: String) {
        wanApi.register(username, password, repassword)
            .subscribe(viewModelScope, {
                if (it.errorCode == Constant.NET_SUCCESS) {
                    showToast("注册成功")
                    userInfo.value = it.data
                } else {
                    showToast("注册失败->" + it.errorMsg)
                }
            })
    }
}