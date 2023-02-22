package com.wislie.wanandroid.adapter.holder

import android.os.Bundle
import com.wislie.common.base.BaseVHolder
import com.wislie.common.ext.findNav
import com.wislie.wanandroid.R
import com.wislie.wanandroid.data.ArticleInfo
import com.wislie.wanandroid.databinding.ItemFirstPageArticleBinding
import com.wislie.wanandroid.util.Settings
import com.wislie.wanandroid.ext.startLogin
import com.wislie.wanandroid.util.ArticleType

class FirstPageArticleHolder(
    override val binding: ItemFirstPageArticleBinding,
    val collect: (articleInfo: ArticleInfo?) -> Unit
) :
    BaseVHolder<ArticleInfo>(binding) {

    private var index = 0

    init {
        binding.root.setOnClickListener { v ->
            val bundle = Bundle().apply {
                putInt("type", ArticleType.TYPE_LIST_ARTICLE)
                putInt("id", binding.articleInfo?.id ?: 0)
                putString("title", binding.articleInfo?.title)
                putString("author", binding.articleInfo?.author)
                putString("linkUrl", binding.articleInfo?.link)
                putBoolean("collect", binding.articleInfo?.collect ?: false)
            }
            v.findNav().navigate(R.id.fragment_web, bundle)
        }

        binding.ivCollect.setOnClickListener { // 在onBindViewHolder中点击不合理
            if (!Settings.isLogined) {
                it.startLogin()
                return@setOnClickListener
            }
            collect.invoke(binding.articleInfo)
        }

        binding.tvAuthor.setOnClickListener { v->
            val bundle = Bundle().apply {
                putInt("id", binding.articleInfo?.id ?: -1)
                putString("author", binding.articleInfo?.author)
            }
            v.findNav().navigate(R.id.fragment_share_author_article_list, bundle)
        }
    }

    override fun bind(data: ArticleInfo?, position: Int) {
        this.index = position
        binding.articleInfo = data
        binding.executePendingBindings()
    }
}