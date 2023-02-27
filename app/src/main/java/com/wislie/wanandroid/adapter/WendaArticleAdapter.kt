package com.wislie.wanandroid.adapter

import com.wislie.common.base.BaseAdapter
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.callback.articleCallback
import com.wislie.wanandroid.adapter.holder.WendaArticleHolder
import com.wislie.wanandroid.data.ArticleInfo
import com.wislie.wanandroid.databinding.ItemWendaArticleBinding

class WendaArticleAdapter(val collect: (articleInfo: ArticleInfo?) -> Unit) :
    BaseAdapter<ArticleInfo, ItemWendaArticleBinding, WendaArticleHolder>(
        articleCallback
    ) {
    override fun onCreateViewHolder(binding: ItemWendaArticleBinding): WendaArticleHolder {
        return WendaArticleHolder(binding, collect)
    }

    override fun getItemLayoutId(): Int {
        return R.layout.item_wenda_article
    }
}







