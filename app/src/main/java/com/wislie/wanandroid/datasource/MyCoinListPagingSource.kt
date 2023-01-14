package com.wislie.wanandroid.datasource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wislie.wanandroid.data.CoinItem
import com.wislie.wanandroid.network.apiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 我的积分列表
 */
class MyCoinListPagingSource(private val pageSize:Int) : PagingSource<Long, CoinItem>() {

    override val keyReuseSupported: Boolean
        get() = true //不重写会闪退

    override fun getRefreshKey(state: PagingState<Long, CoinItem>): Long? = null

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, CoinItem> {

        return withContext(Dispatchers.IO) {
            val currentPage = params.key ?: 0
            try {
                val myCoinListResp = apiService.getMyCoinList(currentPage,pageSize)
                //当前页码小于总页码页面加1
                var nextPage =
                    if (currentPage < myCoinListResp?.data?.pageCount ?: 0) currentPage + 1 else null

                if (myCoinListResp != null) {
                    Log.i("wislieZhu","currentPage=$currentPage nextPage=$nextPage size=${myCoinListResp.data?.datas?.size}  params.loadSize=${params.loadSize}")
                    LoadResult.Page(
                        data = myCoinListResp.data?.datas ?: listOf(),
                        prevKey = null,
                        nextKey = nextPage
                    )
                } else {
                    LoadResult.Error(throwable = Throwable())
                }


                /*if (myCoinListResp.errorCode == 0) {
                    //上一页
                    val prevPage = if (currentPage >= 1) currentPage - 1 else null
                    //下一页
                    var nextPage = currentPage + 1

                    myCoinListResp.data?.also {
                        if (it.pageCount <= nextPage) {
                            nextPage = currentPage
                        }
                    }
                    Log.i("wislieZhu"," prevPage=$prevPage nextPage=$nextPage")
                    LoadResult.Page(myCoinListResp.data?.datas ?: listOf(), prevPage, nextPage)
                } else {
                    LoadResult.Error(Exception(myCoinListResp.errorMsg))
                }*/
            } catch (e: java.lang.Exception) {
                LoadResult.Error(e)
            }

        }
    }
}