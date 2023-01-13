package com.wislie.wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.wislie.common.base.BaseViewModel
import com.wislie.common.base.ResultState
import com.wislie.common.base.request
import com.wislie.wanandroid.data.HotKey
import com.wislie.wanandroid.datasource.ArticleSearchPagingSource
import com.wislie.wanandroid.network.apiService

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
            PagingConfig(PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { ArticleSearchPagingSource(hotKey) })
            .flow
            .cachedIn(viewModelScope)


}