package com.wislie.wanandroid.data

/**
 * 个人积分
 */
data class Coin(
    val coinCount: Int,
    val level: Int,
    val nickname: String,
    val rank: String,
    val userId: Int,
    val username: String
)