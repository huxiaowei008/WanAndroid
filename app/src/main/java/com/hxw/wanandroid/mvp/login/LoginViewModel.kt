package com.hxw.wanandroid.mvp.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hxw.core.base.exceptionHandler
import com.hxw.core.utils.showToast
import com.hxw.wanandroid.Constant
import com.hxw.wanandroid.WanApi
import com.hxw.wanandroid.entity.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.await

/**
 * @author hxw
 * @date 2019/1/24
 */
class LoginViewModel(private val wanApi: WanApi) : ViewModel() {


    val userInfo = MutableLiveData<UserEntity>()

    fun login(username: String, password: String) {
        viewModelScope.launch(Dispatchers.Main + exceptionHandler) {
            val result = wanApi.login(username, password).await()
            if (result.errorCode == Constant.NET_SUCCESS) {
                showToast("登陆成功")
                userInfo.value = result.data
            } else {
                showToast("登陆失败->" + result.errorMsg)
            }
        }
    }

    fun register(username: String, password: String, repassword: String) {
        viewModelScope.launch(Dispatchers.Main + exceptionHandler) {
            val result = wanApi.register(username, password, repassword).await()
            if (result.errorCode == Constant.NET_SUCCESS) {
                showToast("注册成功")
                userInfo.value = result.data
            } else {
                showToast("注册失败->" + result.errorMsg)
            }
        }
    }
}