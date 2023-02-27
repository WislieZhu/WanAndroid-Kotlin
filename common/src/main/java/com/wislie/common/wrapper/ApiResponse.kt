package com.wislie.common.wrapper

data class ApiResponse<T>(
    val errorCode: Int = 0,
    val errorMsg: String = "",
    val data: T
)
