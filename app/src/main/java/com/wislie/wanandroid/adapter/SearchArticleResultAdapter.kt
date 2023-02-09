package com.wislie.wanandroid.adapter

import com.wislie.common.base.BaseAdapter
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.callback.ArticleCallback
import com.wislie.wanandroid.adapter.holder.SearchArticleResultHolder
import com.wislie.wanandroid.data.ArticleInfo
import com.wislie.wanandroid.databinding.ItemFirstPageArticleBinding

/**
 * 搜索的文章列表
 */
class SearchArticleResultAdapter(val collect: (articleInfo: ArticleInfo?) -> Unit)  :
    BaseAdapter<ArticleInfo, ItemFirstPageArticleBinding, SearchArticleResultHolder>(
        ArticleCallback()
    ) {
    override fun onCreateViewHolder(binding: ItemFirstPageArticleBinding): SearchArticleResultHolder {
        return SearchArticleResultHolder(binding, collect)
    }

    override fun getItemLayoutId(): Int {
        return R.layout.item_first_page_article
    }

}



