package com.wislie.wanandroid.adapter

import com.wislie.common.base.BaseAdapter
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.callback.ArticleCallback
import com.wislie.wanandroid.adapter.holder.TreeArticleHolder
import com.wislie.wanandroid.data.ArticleInfo
import com.wislie.wanandroid.databinding.ItemTreeArticleBinding

class TreeArticleAdapter(val collect: (articleInfo: ArticleInfo?) -> Unit) :
    BaseAdapter<ArticleInfo, ItemTreeArticleBinding, TreeArticleHolder>(
        ArticleCallback()
    ) {
    override fun onCreateViewHolder(binding: ItemTreeArticleBinding): TreeArticleHolder {
        return TreeArticleHolder(binding, collect)
    }

    override fun getItemLayoutId(): Int {
        return R.layout.item_tree_article
    }
}







