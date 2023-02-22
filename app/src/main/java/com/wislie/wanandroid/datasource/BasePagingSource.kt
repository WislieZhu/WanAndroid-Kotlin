package com.wislie.wanandroid.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wislie.common.wrapper.ApiPageResponse
import com.wislie.common.wrapper.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BasePagingSource<T : Any>(
    private val initPage: Long,
    private val block: (Long) -> ApiResponse<ApiPageResponse<T>?>
) :
    PagingSource<Long, T>() {

    override val keyReuseSupported: Boolean
        get() = true //不重写会闪退

    override fun getRefreshKey(state: PagingState<Long, T>): Long? = null

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, T> {

        return withContext(Dispatchers.IO) {
            val currentPage = params.key ?: initPage  //initPage 初始值为0,则第一页从0开始;初始值为1,则第一页从1开始
            try {
                val articleListResp =
                    block.invoke(currentPage)  //apiService.getShareAuthorArticles(id, currentPage)
                //当前页码小于总页码页面加1
                var nextPage: Long? = null
                if (articleListResp != null && articleListResp.errorCode == 0) {
                    articleListResp.data?.run {
                        if (initPage == 0L) {
                            if (currentPage < this.pageCount - 1) { //初始值 currentPage为0的情况
                                nextPage = currentPage + 1
                            }
                        } else if (initPage == 1L) { //初始值 currentPage为1的情况
                            if (currentPage < this.pageCount) {
                                nextPage = currentPage + 1
                            }
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