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

    val collectWebsiteListLiveData by lazy {
        MutableLiveData<ResultState<List<CollectWebsiteInfo>?>>()
    }

    /**
     * 收藏的网址列表
     */
    fun getCollectWebsiteList() {
        request({
            apiService.getCollectWebsiteList()
        }, collectWebsiteListLiveData)
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

    fun collect(articleInfo: ArticleInfo) {
        request({
            apiService.collect(articleInfo.id)
        }, {
            collectResultLiveData.value = ResultState.Success(articleInfo)
        }, { exception, errorCode ->
            collectResultLiveData.value = ResultState.Error(exception, errorCode, true)
        }, { loadingMessage, isShowingDialog ->
            collectResultLiveData.value = ResultState.Loading(loadingMessage, isShowingDialog)
        })
    }

    /**
     * 列表->收藏
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
     * 收藏页->收藏
     */
    val collectPageLiveData by lazy {
        MutableLiveData<ResultState<ArticleInfo?>>()
    }

    fun collectPage(title: String, author: String?, link: String) {
        request({
            apiService.collect(title, author, link)
        }, collectPageLiveData)
    }

    /**
     * 取消收藏
     */
    val uncollectLiveData by lazy {
        MutableLiveData<ResultState<Int>>()
    }

    /**
     * 列表->取消收藏
     */
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
     * 收藏页->取消收藏
     */
    fun unCollectPage(id: Int, originId: Int) {
        request({
            apiService.uncollect(id, originId)
        }, {
            uncollectLiveData.value = ResultState.Success(id)
        }, { exception, errorCode ->
            uncollectLiveData.value = ResultState.Error(exception, errorCode, true)
        }, { loadingMessage, isShowingDialog ->
            uncollectLiveData.value = ResultState.Loading(loadingMessage, isShowingDialog)
        })
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
    fun getWxArticleList(id: Int, key: String?) =
        Pager(
            PagingConfig(pageSize = 1),
            pagingSourceFactory = { WxArticlePagingSource(id, key) })
            .flow

    /**
     * 广场文章列表
     */
    val squareArticleList by lazy {
        Pager(
            PagingConfig(pageSize = 1),
            pagingSourceFactory = { SquareArticlePagingSource() })
            .flow
    }

    /**
     * 获取体系列表
     */
    val treeListLiveData by lazy {
        MutableLiveData<ResultState<List<TreeInfo>?>>()
    }

    fun getTreeList() {
        request({
            apiService.getTreeList()
        }, treeListLiveData)
    }

    /**
     * 体系的文章列表
     */
    fun getTreeArticleList(id: Int) =
        Pager(
            PagingConfig(pageSize = 1),
            pagingSourceFactory = { TreeArticlePagingSource(id) })
            .flow

}