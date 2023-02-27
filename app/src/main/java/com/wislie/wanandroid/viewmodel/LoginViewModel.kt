package com.wislie.wanandroid.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.wislie.common.base.BaseViewModel
import com.wislie.common.base.ResultState
import com.wislie.common.base.request
import com.wislie.wanandroid.data.UserInfo
import com.wislie.wanandroid.network.apiService


class LoginViewModel : BaseViewModel() {


    //注册信息
    val registerResultLiveData by lazy {
        MutableLiveData<ResultState<UserInfo?>>()
    }

    fun register(username: String, password: String, repassword: String) {
        request({
            apiService.register(username, password, repassword)
        }, registerResultLiveData)
    }

    //登录信息
    val loginInfoResultLiveData by lazy {
        MutableLiveData<ResultState<UserInfo?>>()
    }

    fun login(username: String, password: String) {
        request({
            apiService.login(username, password)
        }, loginInfoResultLiveData, isShowDialog = true)
    }

    //退出登录
    val logoutResultLiveData by lazy {
        MutableLiveData<ResultState<Any?>>()
    }

    fun logout() {
        request({
            apiService.logout()
        }, logoutResultLiveData, isShowDialog = true)
    }


}