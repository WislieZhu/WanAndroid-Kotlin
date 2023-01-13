package com.wislie.wanandroid.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wislie.wanandroid.data.ArticleInfo
import com.wislie.wanandroid.network.apiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 文章搜索分页
 */
class ArticleSearchPagingSource(val hotKey: String) : PagingSource<Long, ArticleInfo>() {

    override val keyReuseSupported: Boolean
        get() = true //不重写会闪退

    override fun getRefreshKey(state: PagingState<Long, ArticleInfo>): Long? = null

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, ArticleInfo> {

        return withContext(Dispatchers.IO) {
            val currentPage = params.key ?: 0
            try {
                val articleListResp = apiService.queryArticles(currentPage, hotKey)

                if (articleListResp.errorCode == 0) {
                    //上一页
                    val prevPage = if (currentPage >= 1) currentPage - 1 else null
                    //下一页
                    var nextPage = currentPage + 1

                    articleListResp.data?.also {
                        if (it.pageCount <= nextPage) {
                            nextPage = currentPage
                        }
                    }
                    LoadResult.Page(articleListResp.data?.datas ?: listOf(), prevPage, nextPage)
                } else {
                    LoadResult.Error(Exception(articleListResp.errorMsg))
                }
            } catch (e: java.lang.Exception) {
                LoadResult.Error(e)
            }

        }
    }
}