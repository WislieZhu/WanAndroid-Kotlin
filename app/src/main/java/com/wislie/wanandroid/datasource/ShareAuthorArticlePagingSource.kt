package com.wislie.wanandroid.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wislie.common.network.AppException
import com.wislie.common.network.ExceptionHandle
import com.wislie.common.wrapper.ApiResponse
import com.wislie.wanandroid.data.ArticleInfo
import com.wislie.wanandroid.data.ShareAuthorInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 分享者的文章分页
 */
class ShareAuthorArticlePagingSource(private val block: (Long) -> ApiResponse<ShareAuthorInfo?>) :
    PagingSource<Long, ArticleInfo>() {

    override fun getRefreshKey(state: PagingState<Long, ArticleInfo>): Long? = null

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, ArticleInfo> {

        return withContext(Dispatchers.IO) {
            val currentPage = params.key ?: 1
            try {
                val articleListResp = block.invoke(currentPage)
                //当前页码小于总页码页面加1
                var nextPage: Long? = null
                if (articleListResp != null && articleListResp.errorCode == 0) {
                    articleListResp?.data?.shareArticles?.run {
                        if (currentPage < this.pageCount) { //初始值 currentPage为1的情况
                            nextPage = currentPage + 1
                        }
                    }
                    LoadResult.Page(
                        data = articleListResp.data?.shareArticles?.datas ?: listOf(),
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


