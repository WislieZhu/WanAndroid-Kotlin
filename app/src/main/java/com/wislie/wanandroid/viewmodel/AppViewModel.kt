package com.wislie.wanandroid.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.wislie.wanandroid.data.CollectEvent
import com.wislie.wanandroid.data.ToDoInfo
import com.wislie.wanandroid.data.UserInfo

/**
 * app层面的viewModel
 */
class AppViewModel(application: Application):AndroidViewModel(application) {

    //登录之后的用户信息
    val userInfoLiveData:MutableLiveData<UserInfo?>  by lazy {
        MutableLiveData()
    }

    //收藏
    val collectEventLiveData:MutableLiveData<CollectEvent> by lazy {
        MutableLiveData()
    }

    //todo更新
    val todoInfoUpdateLiveData:MutableLiveData<ToDoInfo> by lazy {
        MutableLiveData()
    }

    //todo新增
    val todoInfoAddLiveData:MutableLiveData<ToDoInfo> by lazy {
        MutableLiveData()
    }

    //share新增
    val shareAddLiveData:MutableLiveData<Any> by lazy {
        MutableLiveData()
    }
}