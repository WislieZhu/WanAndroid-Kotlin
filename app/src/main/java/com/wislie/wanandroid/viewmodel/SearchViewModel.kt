package com.wislie.wanandroid.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.wislie.common.base.BaseViewModel
import com.wislie.common.base.ResultState
import com.wislie.common.base.request
import com.wislie.wanandroid.data.HotKey
import com.wislie.wanandroid.datasource.BasePagingSource
import com.wislie.wanandroid.db.AppDatabase
import com.wislie.wanandroid.db.SearchKey
import com.wislie.wanandroid.network.apiService
import kotlinx.coroutines.runBlocking
import java.lang.Exception

class SearchViewModel : BaseViewModel() {

    val hotKeyResultLiveData by lazy {
        MutableLiveData<ResultState<List<HotKey>?>>()
    }

    /**
     * 搜索热词
     */
    fun getHotKey() {
        request({
            apiService.getHotKey()
        }, hotKeyResultLiveData)
    }


    /**
     * 搜索到的文章列表
     */
    fun getArticleList(hotKey: String) =
        Pager(
            PagingConfig(pageSize = 1),
            pagingSourceFactory = {
                BasePagingSource(0) { currentPage ->
                    runBlocking {
                        apiService.queryArticles(currentPage, hotKey)
                    }
                }
            })
            .flow

        /*Pager(
            PagingConfig(pageSize = 1),
            pagingSourceFactory = { ArticleSearchPagingSource(hotKey) })
            .flow*/

    /**
     * 所有的搜索记录
     */
    fun queryAllSearchKey(context: Context) =
        Pager(
            PagingConfig(pageSize = 1),
            pagingSourceFactory = {
                AppDatabase.getDatabaseSingleton(context).getSearchKeyDao().queryAllSearchKey()
            })
            .flow

    val searchKeyLiveData by lazy {
        MutableLiveData<ResultState<SearchKey>>()
    }

    /**
     * 删除一条搜索记录
     */
    fun deleteSearchKeyByName(context: Context, searchKey: SearchKey) {
        val searchKeyDao = AppDatabase.getDatabaseSingleton(context).getSearchKeyDao()
        searchKey?.run {
            val row = searchKeyDao.deleteSearchKeyByName(this.hotKey)
            if (row > 0) {
                searchKeyLiveData.value = ResultState.Success(searchKey)
            } else {
                searchKeyLiveData.value = ResultState.Error(Exception("删除出错了"))
            }
        }
    }

    val searchKeyDelLiveData by lazy {
        MutableLiveData<ResultState<Boolean>>()
    }

    /**
     * 删除所有的搜索记录
     */
    fun deleteSearchHistory(context: Context) {
        val searchKeyDao = AppDatabase.getDatabaseSingleton(context).getSearchKeyDao()
        val row = searchKeyDao.deleteAllSearchHistory()
        if (row > 0) {
            searchKeyDelLiveData.value = ResultState.Success(true)
        }
    }

    /**
     * 插入一条搜索记录
     */
    fun insertSearchKey(context: Context, name: String): Long {
        val searchKeyDao = AppDatabase.getDatabaseSingleton(context).getSearchKeyDao()
        return searchKeyDao.insertSearchKey(SearchKey(name, System.currentTimeMillis()))
    }

}