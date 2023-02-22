package com.wislie.wanandroid.adapter

import com.wislie.common.base.BaseAdapter
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.callback.ArticleCallback
import com.wislie.wanandroid.adapter.holder.SharePrivateArticleHolder
import com.wislie.wanandroid.data.ArticleInfo
import com.wislie.wanandroid.databinding.ItemSharePrivateArticleBinding
import com.wislie.wanandroid.util.SlideHelper

/**
 *  自己的分享的文章
 */
class SharePrivateArticleAdapter(val collect: (articleInfo: ArticleInfo?) -> Unit) :
    BaseAdapter<ArticleInfo, ItemSharePrivateArticleBinding, SharePrivateArticleHolder>(
        ArticleCallback()
    ) {
    override fun onCreateViewHolder(binding: ItemSharePrivateArticleBinding): SharePrivateArticleHolder {
        return SharePrivateArticleHolder(SlideHelper(), binding, collect)
    }

    override fun getItemLayoutId(): Int {
        return R.layout.item_share_private_article
    }
}







