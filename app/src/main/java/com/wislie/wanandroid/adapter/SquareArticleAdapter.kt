package com.wislie.wanandroid.adapter

import com.wislie.common.base.BaseAdapter
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.callback.ArticleCallback
import com.wislie.wanandroid.adapter.holder.SquareArticleHolder
import com.wislie.wanandroid.data.ArticleInfo
import com.wislie.wanandroid.databinding.ItemSquareArticleBinding

/**
 * 广场
 */
class SquareArticleAdapter(val collect: (articleInfo: ArticleInfo?) -> Unit) :
    BaseAdapter<ArticleInfo, ItemSquareArticleBinding, SquareArticleHolder>(
        ArticleCallback()
    ) {
    override fun onCreateViewHolder(binding: ItemSquareArticleBinding): SquareArticleHolder {
        return SquareArticleHolder(binding, collect)
    }

    override fun getItemLayoutId(): Int {
        return R.layout.item_square_article
    }
}







