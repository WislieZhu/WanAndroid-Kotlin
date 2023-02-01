package com.wislie.wanandroid.viewmodel

import androidx.databinding.ObservableField
import com.wislie.common.base.BaseViewModel

/**
 * 登录
 */
class LoginStateViewModel : BaseViewModel() {


    //输入的账号
    val account: ObservableField<String> by lazy {
        ObservableField()
    }


}