package com.wislie.common.wrapper

data class PagerApiResponse<T>(
    val code: Int,
    val msg: String?,
    val aaData: ArrayList<T>,
    val itotalRecords: Int,
    val itotalPages: Int,
    val sEcho: Any?
)
