package com.wislie.wanandroid.adapter

import com.wislie.common.base.BaseAdapter
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.callback.articleCallback
import com.wislie.wanandroid.adapter.holder.WxArticleHolder
import com.wislie.wanandroid.data.ArticleInfo
import com.wislie.wanandroid.databinding.ItemWxArticleBinding

class WxArticleAdapter(val collect: (articleInfo: ArticleInfo?) -> Unit) :
    BaseAdapter<ArticleInfo, ItemWxArticleBinding, WxArticleHolder>(
        articleCallback
    ) {
    override fun onCreateViewHolder(binding: ItemWxArticleBinding): WxArticleHolder {
        return WxArticleHolder(binding, collect)
    }

    override fun getItemLayoutId(): Int {
        return R.layout.item_wx_article
    }
}







