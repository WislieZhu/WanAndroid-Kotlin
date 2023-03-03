package com.wislie.wanandroid.adapter

import com.wislie.common.base.BaseAdapter
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.callback.articleCallback
import com.wislie.wanandroid.adapter.holder.CommonArticleHolder
import com.wislie.wanandroid.data.ArticleInfo
import com.wislie.wanandroid.databinding.ItemCommonArticleBinding

class CommonArticleAdapter(val collect: (articleInfo: ArticleInfo?) -> Unit) :
    BaseAdapter<ArticleInfo, ItemCommonArticleBinding, CommonArticleHolder>(
        articleCallback
    ) {
    override fun onCreateViewHolder(binding: ItemCommonArticleBinding): CommonArticleHolder {
        return CommonArticleHolder(binding, collect)
    }

    override fun getItemLayoutId(): Int {
        return R.layout.item_common_article
    }
}







