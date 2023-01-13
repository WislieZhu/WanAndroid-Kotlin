package com.wislie.common.wrapper

data class ApiPageResponse<P>(
    val curPage: Int,
    val datas: ArrayList<P>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)
