package com.wislie.wanandroid.adapter.holder

import android.os.Bundle
import com.wislie.common.base.BaseVHolder
import com.wislie.common.ext.findNav
import com.wislie.wanandroid.R
import com.wislie.wanandroid.data.ArticleInfo
import com.wislie.wanandroid.databinding.ItemSquareArticleBinding
import com.wislie.wanandroid.util.Settings
import com.wislie.wanandroid.ext.startLogin
import com.wislie.wanandroid.util.ArticleType

/**
 * 广场
 */
class SquareArticleHolder(
    override val binding: ItemSquareArticleBinding,
    val collect: (articleInfo: ArticleInfo?) -> Unit
) :
    BaseVHolder<ArticleInfo>(binding) {


    init {
        binding.root.setOnClickListener { v ->
            val bundle = Bundle()
            bundle.run {
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
    }


    override fun bind(data: ArticleInfo?, position: Int) {
        binding.articleInfo = data
        binding.executePendingBindings()
    }
}