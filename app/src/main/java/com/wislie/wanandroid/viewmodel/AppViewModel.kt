package com.wislie.wanandroid.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.wislie.wanandroid.data.UserInfo

/**
 * app层面的viewModel
 */
class AppViewModel(application: Application):AndroidViewModel(application) {

    //登录之后的用户信息
    val userInfoLiveData:MutableLiveData<UserInfo?>  by lazy {
        MutableLiveData()
    }


}