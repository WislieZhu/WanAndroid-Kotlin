package com.wislie.wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.wislie.common.base.BaseViewModel
import com.wislie.common.base.ResultState
import com.wislie.common.base.request
import com.wislie.wanandroid.data.ToDoInfo
import com.wislie.wanandroid.datasource.TodoListPagingSource
import com.wislie.wanandroid.network.apiService

/**
 * todo相关
 */
class TodoViewModel : BaseViewModel() {

    //todo列表
    val todoList by lazy {
        Pager(
            PagingConfig(pageSize = 1),
            pagingSourceFactory = { TodoListPagingSource() })
            .flow
    }

     val todoLiveData:MutableLiveData<ResultState<ToDoInfo?>> = MutableLiveData()

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
            apiService.updateTodo(id,title, content, date, status, type, priority)
        },todoLiveData, isShowDialog = true)
    }

}

//Failure(java.io.FileNotFoundException: /storage/emulated/0/Pictures/Screenshots/Screenshot_20221222_165319_frontier_defense.jiuqitech.cn.jpg: open failed: EACCES (Permission denied))
//java.net.UnknownServiceException: CLEARTEXT communication to 192.168.0.148 not permitted by network security policy