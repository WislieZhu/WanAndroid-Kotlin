package com.wislie.wanandroid.adapter.holder

import android.os.Bundle
import com.wislie.common.base.BaseVHolder
import com.wislie.common.ext.findNav
import com.wislie.wanandroid.R
import com.wislie.wanandroid.data.ArticleInfo
import com.wislie.wanandroid.databinding.ItemWxArticleBinding
import com.wislie.wanandroid.ext.startLogin
import com.wislie.wanandroid.util.*

/**
 * 微信公众号文章
 */
class WxArticleHolder(
    override val binding: ItemWxArticleBinding,
    val collect: (articleInfo: ArticleInfo?) -> Unit
) :
    BaseVHolder<ArticleInfo>(binding) {


    init {
        binding.root.setOnClickListener { v ->
            val bundle = Bundle()
            bundle.run {
                putInt(ARTICLE_TYPE, ArticleType.TYPE_LIST_ARTICLE)
                putInt(ARTICLE_ID, binding.articleInfo?.id ?: 0)
                putString(ARTICLE_TITLE, binding.articleInfo?.title)
                putString(ARTICLE_AUTHOR, binding.articleInfo?.author)
                putString(ARTICLE_LINK, binding.articleInfo?.link)
                putBoolean(ARTICLE_COLLECT, binding.articleInfo?.collect ?: false)
            }
            v.findNav().navigate(R.id.fragment_web, bundle)
        }

        binding.ivCollect.setOnClickListener { // 在onBindViewHolder中点击不合理
            if (!Settings.logined) {
                it.startLogin()
                return@setOnClickListener
            }
            collect.invoke(binding.articleInfo)
        }
    }


    override fun bind(data: ArticleInfo?, position: Int) {
        binding.articleInfo = data
        binding.executePendingBindings()
    }
}