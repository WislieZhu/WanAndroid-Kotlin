package com.wislie.wanandroid.adapter.holder

import com.wislie.common.base.BaseVHolder
import com.wislie.wanandroid.data.ArticleInfo
import com.wislie.wanandroid.databinding.ItemFirstPageArticleBinding
import com.wislie.wanandroid.util.Settings
import com.wislie.wanandroid.ext.startLogin

class WendaArticleHolder(
    override val binding: ItemFirstPageArticleBinding,
    private val wenda:(Int?)->Unit,
    val collect: (articleInfo: ArticleInfo?) -> Unit
) :
    BaseVHolder<ArticleInfo>(binding) {


    init {
        binding.root.setOnClickListener {
            wenda.invoke(binding.articleInfo?.id)
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