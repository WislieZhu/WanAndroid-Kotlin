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
import com.wislie.wanandroid.data.CollectWebsiteInfo
import com.wislie.wanandroid.data.ProjectCategory
import com.wislie.wanandroid.datasource.*
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
     * 收藏的文章列表
     */
    val collectArticleList by lazy {
        Pager(
            PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { CollectArticlePagingSource() })
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


    val collectWebsitesLiveData by lazy {
        MutableLiveData<ResultState<List<CollectWebsiteInfo>?>>()
    }

    /**
     * 收藏的网址列表
     */
    fun getCollectWebsites(){
        request({
            apiService.getCollectWebsites()
        },collectWebsitesLiveData)
    }

    /**
     * 删除收藏网址
     */
    val delCollectWebsiteLiveData  by lazy {
        MutableLiveData<ResultState<Int>>()
    }

    fun delCollectWebsite(id: Int){
        request({
            apiService.deleteCollectWebsite(id)
        },{
            delCollectWebsiteLiveData.value = ResultState.Success(id)
        },{exception, errorCode ->
            delCollectWebsiteLiveData.value = ResultState.Error(exception, errorCode)
        },{ loadingMessage, isShowingDialog ->
            delCollectWebsiteLiveData.value = ResultState.Loading(loadingMessage, isShowingDialog)
        })
    }

    /**
     * 列表收藏
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
            collectResultLiveData.value = ResultState.Loading(loadingMessage, isShowingDialog)
        })
    }

    /**
     * webFragment中收藏
     */
    val collectLiveData by lazy {
        MutableLiveData<ResultState<Int>>()
    }

    fun collect(articleId: Int) {
        request({
            apiService.collect(articleId)
        }, {
            collectLiveData.value = ResultState.Success(articleId)
        }, { exception, errorCode ->
            collectLiveData.value = ResultState.Error(exception, errorCode, true)
        }, { loadingMessage, isShowingDialog ->
            collectLiveData.value = ResultState.Loading(loadingMessage, isShowingDialog)
        }, isShowDialog = true)
    }

    /**
     * 列表取消收藏
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
            uncollectResultLiveData.value = ResultState.Loading(loadingMessage, isShowingDialog)
        })
    }

    /**
     * webFragment中取消收藏
     */
    val uncollectLiveData by lazy {
        MutableLiveData<ResultState<Int>>()
    }

    fun uncollect(articleId: Int) {
        request({
            apiService.uncollect(articleId)
        }, {
            uncollectLiveData.value = ResultState.Success(articleId)
        }, { exception, errorCode ->
            uncollectLiveData.value = ResultState.Error(exception, errorCode, true)
        }, { loadingMessage, isShowingDialog ->
            uncollectLiveData.value = ResultState.Loading(loadingMessage, isShowingDialog)
        }, isShowDialog = true)
    }

}