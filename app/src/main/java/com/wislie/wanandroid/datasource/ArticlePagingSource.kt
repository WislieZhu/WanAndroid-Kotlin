package com.wislie.wanandroid.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
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
                        articleListResp?.data?.also { pageResp ->
                            pageResp.datas.addAll(0, topArticleList)
                        }
                    }
                }
                if(articleListResp.errorCode == 0){
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
                }else{
                    LoadResult.Error(Exception(articleListResp.errorMsg))
                }
            } catch (e: java.lang.Exception) {
                LoadResult.Error(e)
            }

        }


    }
}