package com.wislie.wanandroid.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wislie.wanandroid.data.CoinItem
import com.wislie.wanandroid.network.apiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 我的积分列表
 */
class MyCoinListPagingSource : PagingSource<Long, CoinItem>() {

    override val keyReuseSupported: Boolean
        get() = true //不重写会闪退

    override fun getRefreshKey(state: PagingState<Long, CoinItem>): Long? = null

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, CoinItem> {

        return withContext(Dispatchers.IO) {
            val currentPage = params.key ?: 1 //currentPage 有些是从0开始，有些是从1开始，有点乱
            try {
                val myCoinListResp = apiService.getMyCoinList(currentPage)
                //当前页码小于总页码页面加1
                var nextPage: Long? = null
                if (myCoinListResp != null && myCoinListResp.errorCode == 0) {
                    myCoinListResp.data?.run {
                        if (currentPage  < this.pageCount) {
                            nextPage = currentPage + 1
                        }
                    }
                    LoadResult.Page(
                        data = myCoinListResp.data?.datas ?: listOf(),
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