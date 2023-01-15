package com.wislie.wanandroid.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wislie.wanandroid.data.ArticleInfo
import com.wislie.wanandroid.network.apiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 文章分类
 */
class ArticleCategoryPagingSource(private val cid: Int) : PagingSource<Long, ArticleInfo>() {

    override val keyReuseSupported: Boolean
        get() = true //不重写会闪退

    override fun getRefreshKey(state: PagingState<Long, ArticleInfo>): Long? = null

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, ArticleInfo> {

        return withContext(Dispatchers.IO) {
            val currentPage = params.key ?: 0
            try {

                val articleListResp = if (cid == 0)
                    apiService.getProjectLatest(currentPage)
                else apiService.getProjectByCategory(currentPage, cid)
                //当前页码小于总页码页面加1
                var nextPage: Long? = null
                if (articleListResp != null && articleListResp.errorCode == 0) {
                    articleListResp.data?.run {
                        if (currentPage + 1 < this.pageCount) {
                            nextPage = currentPage + 1
                        }
                    }
                    LoadResult.Page(articleListResp.data?.datas ?: listOf(), null, nextPage)
                } else {
                    LoadResult.Error(Exception(articleListResp.errorMsg))
                }
            } catch (e: java.lang.Exception) {
                LoadResult.Error(e)
            }

        }
    }
}