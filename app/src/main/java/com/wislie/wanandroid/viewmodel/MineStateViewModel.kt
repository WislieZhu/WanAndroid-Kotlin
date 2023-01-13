package com.wislie.wanandroid.viewmodel

import androidx.databinding.ObservableField
import com.wislie.common.base.BaseViewModel
import com.wislie.wanandroid.data.Coin

/**
 * 我的
 */
class MineStateViewModel : BaseViewModel() {


    //没有值的话 显示--
    val coin: ObservableField<Coin> by lazy {
        ObservableField()
    }
}