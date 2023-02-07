package com.wislie.wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.wislie.common.base.BaseViewModel
import com.wislie.common.base.ResultState
import com.wislie.common.base.request
import com.wislie.wanandroid.data.*
import com.wislie.wanandroid.datasource.*
import com.wislie.wanandroid.network.apiService


class ArticlesViewModel : BaseViewModel() {

    val articleList by lazy {
        Pager(
            PagingConfig(pageSize = 1),
            pagingSourceFactory = { ArticlePagingSource() })
            .flow
    }

    val wendaArticleList by lazy {
        Pager(
            PagingConfig(pageSize = 1),
            pagingSourceFactory = { WendaArticlePagingSource() })
            .flow
    }

    /**
     * 收藏的文章列表
     */
    val collectArticleList by lazy {
        Pager(
            PagingConfig(pageSize = 1),
            pagingSourceFactory = { CollectArticlePagingSource() })
            .flow
    }

    /**
     * 问答评论列表
     */
    fun getWendaCommentList(id: Int) =
        Pager(
            PagingConfig(pageSize = 1),
            pagingSourceFactory = { WendaCommentPagingSource(id) })
            .flow


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
            PagingConfig(pageSize = 1),
            pagingSourceFactory = { ArticleCategoryPagingSource(cid) })
            .flow


    val usualWebsiteLiveData by lazy {
        MutableLiveData<ResultState<List<UsualWebsite>?>>()
    }

    /**
     * 获取常用网站 todo 感觉都没啥用
     */
    fun getUsualWebsite() {
        request({
            apiService.getUsualWebsite()
        }, usualWebsiteLiveData)
    }

    val collectWebsitesLiveData by lazy {
        MutableLiveData<ResultState<List<CollectWebsiteInfo>?>>()
    }

    /**
     * 收藏的网址列表
     */
    fun getCollectWebsites() {
        request({
            apiService.getCollectWebsites()
        }, collectWebsitesLiveData)
    }

    val addCollectWebsiteLiveData by lazy {
        MutableLiveData<ResultState<CollectWebsiteInfo?>>()
    }

    /**
     * 添加收藏网址
     */
    fun addCollectWebSite(name: String, link: String) {
        request({
            apiService.addCollectWebsite(name, link)
        }, addCollectWebsiteLiveData)
    }

    /**
     * 删除收藏网址
     */
    val delCollectWebsiteLiveData by lazy {
        MutableLiveData<ResultState<Int>>()
    }

    fun delCollectWebsite(id: Int) {
        request({
            apiService.deleteCollectWebsite(id)
        }, {
            delCollectWebsiteLiveData.value = ResultState.Success(id)
        }, { exception, errorCode ->
            delCollectWebsiteLiveData.value = ResultState.Error(exception, errorCode)
        }, { loadingMessage, isShowingDialog ->
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

    fun unCollect(articleInfo: ArticleInfo, position: Int) {
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
     * 我的收藏页面 取消收藏
     */
    fun unCollectPage(articleInfo: ArticleInfo, position: Int) {
        request({
            apiService.uncollect(articleInfo.id, articleInfo.originId ?: -1)
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

    fun unCollect(articleId: Int) {
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


    /**
     * 获取微信公众号列表
     */
    val wxAccountListLiveData by lazy {
        MutableLiveData<ResultState<List<WxAccountInfo>?>>()
    }

    fun getWxAccountList() {
        request({
            apiService.getWxAccountList()
        }, wxAccountListLiveData)
    }

    /**
     * 公众号文章列表
     */
    fun getWxArticleList(id: Int, key: String? = null) =
        Pager(
            PagingConfig(pageSize = 1),
            pagingSourceFactory = { WxArticlePagingSource(id, key) })
            .flow


}