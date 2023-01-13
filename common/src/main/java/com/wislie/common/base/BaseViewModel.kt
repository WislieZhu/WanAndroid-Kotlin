package com.wislie.common.base

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wislie.common.util.LiveDataBusManager
import com.wislie.common.wrapper.ApiResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class BaseViewModel : ViewModel() {

    companion object{
        const val PAGE_SIZE = 15
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
            Log.i("wislieZhu", "request postLoading isShowDialog=true")
            liveData.value = ResultState.Loading(loadingMessage, true)
        }
        runCatching {
            withContext(defaultDispatcher) {
                block()
            }
        }.onSuccess { result ->
            if (isShowDialog) {
                Log.i("wislieZhu", "request postLoading isShowDialog=false")
                liveData.value = ResultState.Loading(loadingMessage, false)
            }
            //处理成功的情况
            when (result.errorCode) {
                0 -> liveData.value = ResultState.Success(result.data)
                else -> liveData.value =
                    ResultState.Error(Exception(result.errorMsg), result.errorCode)
            }
        }.onFailure { error ->
            if (isShowDialog) {
                Log.i("wislieZhu", "request postLoading isShowDialog=false")
                liveData.value = ResultState.Loading(loadingMessage, false)
            }
            //处理失败的情况
            liveData.value = ResultState.Error(Exception(error))
        }
    }
}


fun <T> BaseViewModel.request(
    block: suspend () -> ApiResponse<T?>,
    success: (ApiResponse<T?>) -> Unit,
    failed: (Exception, Int) -> Unit,
    onLoading:(String, Boolean) -> Unit,
    isShowDialog: Boolean = false,
    loadingMessage: String = "请求网络中...",
    defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    viewModelScope.launch {  //main线程
        if (isShowDialog) {
            Log.i("wislieZhu", "request postLoading isShowDialog=true")
            onLoading(loadingMessage, true)
        }
        runCatching {
            withContext(defaultDispatcher) {
                block()
            }
        }.onSuccess { result ->
            if (isShowDialog) {
                Log.i("wislieZhu", "request postLoading isShowDialog=false")
                onLoading(loadingMessage, false)
            }
            //处理成功的情况
            when (result.errorCode) {
                0 -> success(result)
                else -> failed(Exception(result.errorMsg), result.errorCode)
            }
        }.onFailure { error ->
            if (isShowDialog) {
                Log.i("wislieZhu", "request postLoading isShowDialog=false")
                onLoading(loadingMessage, false)
            }
            //处理失败的情况
            failed(Exception(error), 0)
        }
    }
}


