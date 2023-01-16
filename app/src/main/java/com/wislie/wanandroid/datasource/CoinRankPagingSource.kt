package com.wislie.wanandroid.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wislie.wanandroid.data.CoinItem
import com.wislie.wanandroid.data.CoinRankInfo
import com.wislie.wanandroid.network.apiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 积分排行榜
 */
class CoinRankPagingSource : PagingSource<Long, CoinRankInfo>() {

    override val keyReuseSupported: Boolean
        get() = true //不重写会闪退

    override fun getRefreshKey(state: PagingState<Long, CoinRankInfo>): Long? = null

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, CoinRankInfo> {

        return withContext(Dispatchers.IO) {
            val currentPage = params.key ?: 1
            try {
                val coinRankListResp = apiService.getCoinRank(currentPage)
                //当前页码小于总页码页面加1
                var nextPage: Long? = null
                if (coinRankListResp != null && coinRankListResp.errorCode == 0) {
                    coinRankListResp.data?.run {
                        if (currentPage  < this.pageCount) {
                            nextPage = currentPage + 1
                        }
                    }
                    LoadResult.Page(
                        data = coinRankListResp.data?.datas ?: listOf(),
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