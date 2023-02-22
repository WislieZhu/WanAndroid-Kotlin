package com.wislie.common.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wislie.common.network.AppException
import com.wislie.common.network.ExceptionHandle
import com.wislie.common.wrapper.ApiResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class BaseViewModel : ViewModel() {

    /**
     * PagingDataAdapter 删除item 摘自https://blog.csdn.net/haha223545/article/details/121040837
     */
    private val mTempRemoveFlow = MutableStateFlow(mutableListOf<Any>())
    val mRemovedFlow: Flow<MutableList<Any>> get() = mTempRemoveFlow

    /**
     * 删除item
     */
    fun removeFlowItem(item: Any?) {
        if (item == null) {
            return
        }
        val tempRemoveFlow = mTempRemoveFlow.value
        val removeItem = mutableListOf(item)
        removeItem.addAll(tempRemoveFlow)
        mTempRemoveFlow.value = removeItem
    }

    /**
     * 删除所有item
     */
    fun removeAllItems(){
        mTempRemoveFlow.value = mutableListOf()
    }
}

fun <T> BaseViewModel.request(
    block: suspend () -> ApiResponse<T?>,
    liveData: MutableLiveData<ResultState<T?>>,
    isShowDialog: Boolean = false,
    loadingMessage: String = "请求网络中...",
    defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    viewModelScope.launch {  //main线程
        if (isShowDialog) {
            liveData.value = ResultState.Loading(loadingMessage, true)
        }

        runCatching {
            withContext(defaultDispatcher) {
                block()
            }
        }.onSuccess { result ->
            if (isShowDialog) {
                liveData.value = ResultState.Loading(loadingMessage, false)
            }
            //处理成功的情况
            when (result.errorCode) {
                0 -> liveData.value = ResultState.Success(result.data)
                else -> liveData.value =
                    ResultState.Error(AppException(result.errorCode,result.errorMsg), result.errorCode)
            }
        }.onFailure { error ->
            if (isShowDialog) {
                liveData.value = ResultState.Loading(loadingMessage, false)
            }
            //处理失败的情况
            liveData.value = ResultState.Error(ExceptionHandle.handleException(error))
        }
    }
}


fun <T> BaseViewModel.request(
    block: suspend () -> ApiResponse<T?>,
    success: (ApiResponse<T?>) -> Unit,
    failed: (AppException, Int) -> Unit,
    onLoading: (String, Boolean) -> Unit,
    isShowDialog: Boolean = false,
    loadingMessage: String = "请求网络中...",
    defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    viewModelScope.launch {  //main线程
        if (isShowDialog) {
            onLoading(loadingMessage, true)
        }
        runCatching {
            withContext(defaultDispatcher) {
                block()
            }
        }.onSuccess { result ->
            if (isShowDialog) {
                onLoading(loadingMessage, false)
            }
            //处理成功的情况
            when (result.errorCode) {
                0 -> success(result)
                else -> failed(AppException(result.errorCode,result.errorMsg), result.errorCode) //Exception(result.errorMsg)
            }
        }.onFailure { error ->
            if (isShowDialog) {
                onLoading(loadingMessage, false)
            }
            //处理失败的情况
            failed(ExceptionHandle.handleException(error), 0)
        }
    }
}


