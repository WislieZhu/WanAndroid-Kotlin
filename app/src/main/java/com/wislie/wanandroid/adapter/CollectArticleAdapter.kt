package com.wislie.wanandroid.adapter

import com.wislie.common.base.BaseAdapter
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.callback.ArticleCallback
import com.wislie.wanandroid.adapter.holder.CollectArticleHolder
import com.wislie.wanandroid.data.ArticleInfo
import com.wislie.wanandroid.databinding.ItemCollectArticleBinding

class CollectArticleAdapter(val collect: (Int, articleInfo: ArticleInfo?) -> Unit) :
    BaseAdapter<ArticleInfo, ItemCollectArticleBinding, CollectArticleHolder>(
        ArticleCallback()
    ) {
    override fun onCreateViewHolder(binding: ItemCollectArticleBinding): CollectArticleHolder {
        return CollectArticleHolder(binding, collect)
    }

    override fun getItemLayoutId(): Int {
        return R.layout.item_collect_article
    }
}







