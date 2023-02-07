package com.wislie.wanandroid.data

/**
 * 常用网站
 */
data class UsualWebsite(
    val category: String,
    val icon: String,
    val id: Int,
    val link: String,
    val name: String,
    val order: Int,
    val visible: Int,
    var collect: Boolean = false, //是否收藏, 默认未收藏
)