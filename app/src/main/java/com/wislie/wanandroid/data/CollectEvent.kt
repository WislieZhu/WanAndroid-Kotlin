package com.wislie.wanandroid.data

/**
 * 点赞事件
 */
data class CollectEvent(
    var collect: Boolean, //是否收藏
    val id: Int,
    var author: String? = null,
    var title: String? = null,
    var link: String? = null,
)
