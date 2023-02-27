package com.wislie.wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.wislie.common.base.BaseViewModel
import com.wislie.common.base.ResultState
import com.wislie.common.base.request
import com.wislie.wanandroid.data.ToDoInfo
import com.wislie.wanandroid.datasource.BasePagingSource
import com.wislie.wanandroid.network.apiService
import kotlinx.coroutines.runBlocking

/**
 * todo相关
 */
class TodoViewModel : BaseViewModel() {

    //todo列表
    val todoList by lazy {

        Pager(
            PagingConfig(pageSize = 1),
            pagingSourceFactory = {
                BasePagingSource(1) { currentPage ->
                    runBlocking {
                        apiService.getTodoList(currentPage)
                    }
                }
            })
            .flow

        /*Pager(
            PagingConfig(pageSize = 1),
            pagingSourceFactory = { TodoListPagingSource() })
            .flow*/
    }

    val todoUpdateLiveData: MutableLiveData<ResultState<ToDoInfo?>> = MutableLiveData()

    //更新todo
    fun updateTodo(
        id: Int,
        title: String,
        content: String,
        date: String,
        status: Int,
        type: Int,
        priority: Int
    ) {
        request({
            apiService.updateTodo(id, title, content, date, status, type, priority)
        }, todoUpdateLiveData, isShowDialog = true)
    }

    val todoAddLiveData: MutableLiveData<ResultState<ToDoInfo?>> = MutableLiveData()

    //添加todo
    fun addTodo(
        title: String,
        content: String,
        date: String,
        type: Int, //生活1,工作2,娱乐3
        priority: Int
    ) {
        request({
            apiService.addTodo(title, content, date, type, priority)
        }, todoAddLiveData, isShowDialog = true)
    }


    val todoDelLiveData: MutableLiveData<ResultState<Int>> = MutableLiveData()

    //删除todo
    fun deleteTodo(
        id: Int
    ) {
        request({
            apiService.deleteTodo(id)
        },{
            todoDelLiveData.value = ResultState.Success(id)
        }, { exception, errorCode ->
            todoDelLiveData.value = ResultState.Error(exception, errorCode)
        }, { loadingMessage, isShowingDialog ->
            todoDelLiveData.value = ResultState.Loading(loadingMessage, isShowingDialog)
        })
    }

    val todoDoneLiveData: MutableLiveData<ResultState<ToDoInfo>> = MutableLiveData()
    //完成todo
    fun doneTodo(
        id: Int
    ) {
        request({
            apiService.doneTodo(id,1)
        },{ todoInfo->
            todoInfo?.data?.run {
                todoDoneLiveData.value = ResultState.Success(this)
            }
        }, { exception, errorCode ->
            todoDoneLiveData.value = ResultState.Error(exception, errorCode)
        }, { loadingMessage, isShowingDialog ->
            todoDoneLiveData.value = ResultState.Loading(loadingMessage, isShowingDialog)
        })
    }
}

//Failure(java.io.FileNotFoundException: /storage/emulated/0/Pictures/Screenshots/Screenshot_20221222_165319_frontier_defense.jiuqitech.cn.jpg: open failed: EACCES (Permission denied))
//java.net.UnknownServiceException: CLEARTEXT communication to 192.168.0.148 not permitted by network security policy