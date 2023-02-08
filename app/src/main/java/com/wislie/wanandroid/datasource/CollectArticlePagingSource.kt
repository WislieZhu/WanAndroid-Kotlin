package com.wislie.wanandroid.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wislie.wanandroid.data.ArticleInfo
import com.wislie.wanandroid.network.apiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 收藏的文章列表分页 TODO 收藏的网址 currentPage显示有问题
 */
class CollectArticlePagingSource : PagingSource<Long, ArticleInfo>() {

    override fun getRefreshKey(state: PagingState<Long, ArticleInfo>): Long? = null

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, ArticleInfo> {

        return withContext(Dispatchers.IO) {
            val currentPage = params.key ?: 0
            try {
                val articleListResp = apiService.getCollectArticles(currentPage)
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
                    LoadResult.Error(Exception(articleListResp.errorMsg))
                }
            } catch (e: java.lang.Exception) {
                LoadResult.Error(e)
            }

        }
    }
}