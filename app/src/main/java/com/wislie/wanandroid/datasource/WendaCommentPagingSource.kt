package com.wislie.wanandroid.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wislie.wanandroid.data.CoinItem
import com.wislie.wanandroid.data.CoinRankInfo
import com.wislie.wanandroid.data.ReplyComment
import com.wislie.wanandroid.network.apiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 问答评论
 */
class WendaCommentPagingSource(private val wendaId:Int) : PagingSource<Long, ReplyComment>() {

    override val keyReuseSupported: Boolean
        get() = true //不重写会闪退

    override fun getRefreshKey(state: PagingState<Long, ReplyComment>): Long? = null

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, ReplyComment> {

        return withContext(Dispatchers.IO) {
            val currentPage = params.key ?: 1
            try {
                val wendaCommentResp = apiService.getWendaComment(wendaId)
                //当前页码小于总页码页面加1
                var nextPage: Long? = null
                if (wendaCommentResp != null && wendaCommentResp.errorCode == 0) {
                    wendaCommentResp.data?.run {
                        if (currentPage  < this.pageCount) {
                            nextPage = currentPage + 1
                        }
                    }
                    LoadResult.Page(
                        data = wendaCommentResp.data?.datas ?: listOf(),
                        prevKey = null,
                        nextKey = nextPage
                    )
                } else {
                    LoadResult.Error(throwable = Throwable())
                }
            } catch (e: java.lang.Exception) {
                LoadResult.Error(e)
            }
        }
    }
}