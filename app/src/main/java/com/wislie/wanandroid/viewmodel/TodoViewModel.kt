package com.wislie.wanandroid.viewmodel

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.wislie.common.base.BaseViewModel
import com.wislie.wanandroid.datasource.TodoListPagingSource

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


}

//Failure(java.io.FileNotFoundException: /storage/emulated/0/Pictures/Screenshots/Screenshot_20221222_165319_frontier_defense.jiuqitech.cn.jpg: open failed: EACCES (Permission denied))
//java.net.UnknownServiceException: CLEARTEXT communication to 192.168.0.148 not permitted by network security policy