package com.wislie.wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.wislie.common.base.BaseViewModel
import com.wislie.common.base.ResultState
import com.wislie.common.base.request
import com.wislie.wanandroid.data.Coin
import com.wislie.wanandroid.datasource.CoinRankPagingSource
import com.wislie.wanandroid.datasource.MyCoinListPagingSource
import com.wislie.wanandroid.network.apiService



/**
 * 积分相关
 */
class CoinViewModel : BaseViewModel() {

    //我的积分
    val coinResultLiveData by lazy {
        MutableLiveData<ResultState<Coin?>>()
    }


    //获取积分
    fun getCoin() {
        request({
            apiService.getCoin()
        }, coinResultLiveData)
    }

    //我的积分列表
    val myCoinList by lazy {
        Pager(
            PagingConfig(pageSize = 1),
            pagingSourceFactory = { MyCoinListPagingSource() })
            .flow
    }

    //积分排行榜
    val coinRankList by lazy {
        Pager(
            PagingConfig(pageSize = 1),
            pagingSourceFactory = { CoinRankPagingSource() })
            .flow
    }

}

//Failure(java.io.FileNotFoundException: /storage/emulated/0/Pictures/Screenshots/Screenshot_20221222_165319_frontier_defense.jiuqitech.cn.jpg: open failed: EACCES (Permission denied))
//java.net.UnknownServiceException: CLEARTEXT communication to 192.168.0.148 not permitted by network security policy