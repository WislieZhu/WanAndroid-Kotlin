package com.wislie.wanandroid.adapter

import com.wislie.common.base.BaseAdapter
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.callback.ArticleCallback
import com.wislie.wanandroid.adapter.holder.WendaArticleHolder
import com.wislie.wanandroid.data.ArticleInfo
import com.wislie.wanandroid.databinding.ItemFirstPageArticleBinding

class WendaArticleAdapter(private val wenda:(Int?)->Unit, val collect: (Int, articleInfo: ArticleInfo?) -> Unit) :
    BaseAdapter<ArticleInfo, ItemFirstPageArticleBinding, WendaArticleHolder>(
        ArticleCallback()
    ) {
    override fun onCreateViewHolder(binding: ItemFirstPageArticleBinding): WendaArticleHolder {
        return WendaArticleHolder(binding,wenda, collect)
    }

    override fun getItemLayoutId(): Int {
        return R.layout.item_first_page_article
    }
}







