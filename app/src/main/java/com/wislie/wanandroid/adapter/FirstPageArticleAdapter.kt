package com.wislie.wanandroid.adapter

import com.wislie.common.base.BaseAdapter
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.callback.ArticleCallback
import com.wislie.wanandroid.adapter.holder.FirstPageArticleHolder
import com.wislie.wanandroid.data.ArticleInfo
import com.wislie.wanandroid.databinding.ItemFirstPageArticleBinding

class FirstPageArticleAdapter(val collect: (articleInfo: ArticleInfo?) -> Unit) :
    BaseAdapter<ArticleInfo, ItemFirstPageArticleBinding, FirstPageArticleHolder>(
        ArticleCallback()
    ) {
    override fun onCreateViewHolder(binding: ItemFirstPageArticleBinding): FirstPageArticleHolder {
        return FirstPageArticleHolder(binding, collect)
    }

    override fun getItemLayoutId(): Int {
        return R.layout.item_first_page_article
    }
}







