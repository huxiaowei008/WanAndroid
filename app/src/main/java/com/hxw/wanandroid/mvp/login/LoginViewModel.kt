package com.hxw.wanandroid.mvp.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hxw.core.base.exceptionMain
import com.hxw.core.utils.showToast
import com.hxw.wanandroid.Constant
import com.hxw.wanandroid.WanApi
import com.hxw.wanandroid.entity.UserEntity
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject


/**
 * @author hxw
 * @date 2019/1/24
 */
class LoginViewModel : ViewModel(), KoinComponent {

    private val wanApi: WanApi by inject()
    val userInfo = MutableLiveData<UserEntity>()

    fun login(username: String, password: String) {
        viewModelScope.launch(exceptionMain) {
            val result = wanApi.login(username, password)
            if (result.errorCode == Constant.NET_SUCCESS) {
                showToast("登陆成功")
                userInfo.value = result.data
            } else {
                showToast("登陆失败->" + result.errorMsg)
            }
        }
    }

    fun register(username: String, password: String, repassword: String) {
        viewModelScope.launch(exceptionMain) {
            val result = wanApi.register(username, password, repassword)
            if (result.errorCode == Constant.NET_SUCCESS) {
                showToast("注册成功")
                userInfo.value = result.data
            } else {
                showToast("注册失败->" + result.errorMsg)
            }
        }
    }
}