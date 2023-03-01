package com.wislie.wanandroid.adapter.holder

import android.os.Bundle
import com.wislie.common.base.BaseVHolder
import com.wislie.common.ext.findNav
import com.wislie.wanandroid.R
import com.wislie.wanandroid.data.ArticleInfo
import com.wislie.wanandroid.databinding.ItemFirstPageArticleBinding
import com.wislie.wanandroid.ext.startLogin
import com.wislie.wanandroid.util.*

class FirstPageArticleHolder(
    override val binding: ItemFirstPageArticleBinding,
    val collect: (articleInfo: ArticleInfo?) -> Unit
) :
    BaseVHolder<ArticleInfo>(binding) {


    init {

        binding.ivCollect.setOnClickListener { v->
            if (!Settings.logined) {
                v.startLogin()
                return@setOnClickListener
            }
            collect.invoke(binding.articleInfo)
        }

        binding.root.setOnClickListener { v ->
            val bundle = Bundle().apply {
                putInt(ARTICLE_TYPE, ArticleType.TYPE_LIST_ARTICLE)
                putInt(ARTICLE_ID, binding.articleInfo?.id ?: 0)
                putString(ARTICLE_TITLE, binding.articleInfo?.title)
                putString(ARTICLE_AUTHOR, binding.articleInfo?.author)
                putString(ARTICLE_LINK, binding.articleInfo?.link)
                putBoolean(ARTICLE_COLLECT, binding.articleInfo?.collect ?: false)
            }
            v.findNav().navigate(R.id.fragment_web, bundle)
        }

        binding.tvAuthor.setOnClickListener { v->
            val bundle = Bundle().apply {
                putInt(ARTICLE_USER_ID, binding.articleInfo?.userId ?: -1)
                putString(ARTICLE_AUTHOR, binding.articleInfo?.author)
            }
            v.findNav().navigate(R.id.fragment_share_author_article_list, bundle)
        }
    }

    override fun bind(data: ArticleInfo?, position: Int) {
        binding.articleInfo = data
        binding.executePendingBindings()
    }
}