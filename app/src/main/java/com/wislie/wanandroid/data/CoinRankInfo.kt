package com.wislie.wanandroid.data

data class CoinRankInfo(
    val coinCount: Int,
    val level: Int,
    val nickname: String,
    val rank: String,
    val userId: Int,
    val username: String,
    var index: Int? = 0
)