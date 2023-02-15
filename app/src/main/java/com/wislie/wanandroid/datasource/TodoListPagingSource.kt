package com.wislie.wanandroid.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wislie.wanandroid.data.CoinItem
import com.wislie.wanandroid.data.CoinRankInfo
import com.wislie.wanandroid.data.ToDoInfo
import com.wislie.wanandroid.network.apiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * TODO列表
 */
class TodoListPagingSource : PagingSource<Long, ToDoInfo>() {

    override val keyReuseSupported: Boolean
        get() = true //不重写会闪退

    override fun getRefreshKey(state: PagingState<Long, ToDoInfo>): Long? = null

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, ToDoInfo> {

        return withContext(Dispatchers.IO) {
            val currentPage = params.key ?: 1
            try {
                val todoListResp = apiService.getTodoList(currentPage)
                //当前页码小于总页码页面加1
                var nextPage: Long? = null
                if (todoListResp != null && todoListResp.errorCode == 0) {
                    todoListResp.data?.run {
                        if (currentPage < this.pageCount) { //初始值 currentPage为1的情况
                            nextPage = currentPage + 1
                        }
                    }
                    LoadResult.Page(
                        data = todoListResp.data?.datas ?: listOf(),
                        prevKey = null,
                        nextKey = nextPage
                    )
                } else {
                    LoadResult.Error(throwable = Throwable())
                }
            } catch (e: java.lang.Exception) {
                LoadResult.Error(e)
            }
        }
    }
}