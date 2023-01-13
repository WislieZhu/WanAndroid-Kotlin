package com.wislie.wanandroid.data

/**
 * 积分列表的每个item
 */
data class CoinItem(
    val coinCount: Int,
    val date: Long,
    val desc: String,
    val id: Int,
    val reason: String,
    val type: Int,
    val userId: Int,
    val userName: String
)