package com.wislie.wanandroid.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wislie.common.network.AppException
import com.wislie.wanandroid.data.ArticleInfo
import com.wislie.wanandroid.network.apiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 文章分页
 */
class ArticlePagingSource : PagingSource<Long, ArticleInfo>() {

    override fun getRefreshKey(state: PagingState<Long, ArticleInfo>): Long? = null

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, ArticleInfo> {

        return withContext(Dispatchers.IO) {
            val currentPage = params.key ?: 0
            try {
                val articleListResp = apiService.getArticleList(currentPage)
                if (currentPage == 0L) {
                    //置顶的文章列表
                    val topArticleListResp = apiService.getTopArticleList()
                    topArticleListResp.data?.let { topArticleList ->
                        articleListResp?.data?.run {
                            datas.addAll(0, topArticleList)
                        }
                    }
                }
                //当前页码小于总页码页面加1
                var nextPage: Long? = null
                if (articleListResp != null && articleListResp.errorCode == 0) {
                    articleListResp?.data?.run {
                        if (currentPage < this.pageCount - 1) { //初始值 currentPage为0的情况
                            nextPage = currentPage + 1
                        }
                    }
                    LoadResult.Page(
                        data = articleListResp.data?.datas ?: listOf(),
                        prevKey = null,
                        nextKey = nextPage
                    )
                } else {
                    LoadResult.Error(AppException(articleListResp.errorCode,articleListResp.errorMsg))
                }
            } catch (e: java.lang.Exception) {
                LoadResult.Error(e)
            }

        }
    }
}