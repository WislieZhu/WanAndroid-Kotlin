package com.wislie.common.base

sealed class ResultState<out R> {
    data class Success<out T>(val data: T, val position: Int = 0) : ResultState<T>()
    data class Error(
        val exception: Exception,
        val errorCode: Int = 0,
        val isNeedLogin: Boolean = false
    ) : ResultState<Nothing>()

    data class Loading(val loadingMessage: String, val isShownDialog: Boolean) :
        ResultState<Nothing>()
}
