package com.wislie.wanandroid.adapter.holder

import android.os.Bundle
import com.wislie.common.base.BaseVHolder
import com.wislie.common.ext.findNav
import com.wislie.wanandroid.R
import com.wislie.wanandroid.data.ArticleInfo
import com.wislie.wanandroid.databinding.ItemFirstPageArticleBinding
//import com.wislie.wanandroid.fragment.SearchArticleFragmentDirections
//import com.wislie.wanandroid.fragment.WebFragmentArgs
import com.wislie.wanandroid.util.Settings
import com.wislie.wanandroid.util.startLogin

class FirstPageArticleHolder(
    override val binding: ItemFirstPageArticleBinding,
    val collect: (Int, articleInfo: ArticleInfo?) -> Unit
) :
    BaseVHolder<ArticleInfo>(binding) {

    private var index = 0

    init {
        binding.root.setOnClickListener { v ->

            val bundle = Bundle()
            bundle.putString("linkUrl", binding.articleInfo?.link ?: "")
            bundle.putBoolean("collect", binding.articleInfo?.collect?:false)
            bundle.putInt("id", binding.articleInfo?.id?:0)
            v.findNav().navigate(R.id.fragment_web, bundle)
        }

        binding.ivCollect.setOnClickListener { // 在onBindViewHolder中点击不合理
            if (!Settings.isLogined) {
                it.startLogin()
                return@setOnClickListener
            }
            collect.invoke(index, binding.articleInfo)
        }
    }


    override fun bind(data: ArticleInfo?, position: Int) {
        this.index = position
        binding.articleInfo = data
        binding.executePendingBindings()
    }
}