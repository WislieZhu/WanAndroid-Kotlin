package com.wislie.wanandroid.adapter.holder

import android.os.Bundle
import com.wislie.common.base.BaseVHolder
import com.wislie.common.ext.findNav
import com.wislie.wanandroid.R
import com.wislie.wanandroid.data.ArticleInfo
import com.wislie.wanandroid.databinding.ItemCollectArticleBinding
import com.wislie.wanandroid.databinding.ItemProjectArticleBinding
import com.wislie.wanandroid.ext.startLogin
import com.wislie.wanandroid.util.*

/**
 * 项目文章
 */
class ProjectArticleHolder(
    override val binding: ItemProjectArticleBinding,
    val collect: (articleInfo: ArticleInfo?) -> Unit
) :
    BaseVHolder<ArticleInfo>(binding) {

    init {
        binding.root.setOnClickListener { v ->
            val bundle = Bundle()
            bundle?.run {
                putInt(ARTICLE_TYPE, ArticleType.TYPE_COLLECT_ARTICLE)
                putInt(ARTICLE_ID, binding.articleInfo?.id ?: 0)
                putInt(ARTICLE_ORIGIN_ID, binding.articleInfo?.originId ?: -1)
                putString(ARTICLE_TITLE, binding.articleInfo?.title)
                putString(ARTICLE_AUTHOR, binding.articleInfo?.author)
                putString(ARTICLE_LINK, binding.articleInfo?.link)
                putBoolean(ARTICLE_COLLECT, true)
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