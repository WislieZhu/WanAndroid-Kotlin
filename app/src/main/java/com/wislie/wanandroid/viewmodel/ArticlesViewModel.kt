package com.wislie.wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.wislie.common.base.BaseViewModel
import com.wislie.common.base.ResultState
import com.wislie.common.base.request
import com.wislie.wanandroid.data.ArticleInfo
import com.wislie.wanandroid.data.Banner
import com.wislie.wanandroid.data.ProjectCategory
import com.wislie.wanandroid.datasource.ArticleCategoryPagingSource
import com.wislie.wanandroid.datasource.ArticlePagingSource
import com.wislie.wanandroid.datasource.WendaArticlePagingSource
import com.wislie.wanandroid.datasource.WendaCommentPagingSource
import com.wislie.wanandroid.network.apiService


class ArticlesViewModel : BaseViewModel() {

    val articleList by lazy {
        Pager(
            PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { ArticlePagingSource() })
            .flow
            .cachedIn(viewModelScope)
    }

    val wendaArticleList by lazy {
        Pager(
            PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { WendaArticlePagingSource() })
            .flow
            .cachedIn(viewModelScope)
    }

    /**
     * 问答评论列表
     */
    fun getWendaCommentList(id: Int) =
        Pager(
            PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { WendaCommentPagingSource(id) })
            .flow
            .cachedIn(viewModelScope)


    val bannerResultLiveData by lazy {
        MutableLiveData<ResultState<List<Banner>?>>()
    }

    fun getBanner() {
        request({
            apiService.getBanner()
        }, bannerResultLiveData)
    }


    val projectCategoryLiveData by lazy {
        MutableLiveData<ResultState<List<ProjectCategory>?>>()
    }

    /**
     * 项目分类
     */
    fun getProjectCategory() {
        request({
            apiService.getProject()
        }, projectCategoryLiveData)
    }

    /**
     * 搜索到的文章列表
     */
    fun getArticleListByCategory(cid: Int) =
        Pager(
            PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { ArticleCategoryPagingSource(cid) })
            .flow
            .cachedIn(viewModelScope)


    /**
     * 收藏
     */
    val collectResultLiveData by lazy {
        MutableLiveData<ResultState<ArticleInfo>>()
    }

    fun collect(articleInfo: ArticleInfo, position: Int) {
        request({
            apiService.collect(articleInfo.id)
        }, {
            collectResultLiveData.value = ResultState.Success(articleInfo, position)
        }, { exception, errorCode ->
            collectResultLiveData.value = ResultState.Error(exception, errorCode, true)
        }, { loadingMessage, isShowingDialog ->

        })
    }

    /**
     * 取消收藏
     */
    val uncollectResultLiveData by lazy {
        MutableLiveData<ResultState<ArticleInfo>>()
    }

    fun uncollect(articleInfo: ArticleInfo, position: Int) {
        request({
            apiService.uncollect(articleInfo.id)
        }, {
            uncollectResultLiveData.value = ResultState.Success(articleInfo, position)
        }, { exception, errorCode ->
            uncollectResultLiveData.value = ResultState.Error(exception, errorCode, true)
        }, { loadingMessage, isShowingDialog ->

        })
    }

}