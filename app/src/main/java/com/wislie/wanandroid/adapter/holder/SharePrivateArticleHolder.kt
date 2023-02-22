package com.wislie.wanandroid.adapter.holder

import android.os.Bundle
import com.d.lib.slidelayout.SlideLayout
import com.wislie.common.base.BaseVHolder
import com.wislie.common.ext.findNav
import com.wislie.wanandroid.R
import com.wislie.wanandroid.data.ArticleInfo
import com.wislie.wanandroid.databinding.ItemSharePrivateArticleBinding
import com.wislie.wanandroid.databinding.ItemWxArticleBinding
import com.wislie.wanandroid.util.Settings
import com.wislie.wanandroid.ext.startLogin
import com.wislie.wanandroid.util.ArticleType
import com.wislie.wanandroid.util.SlideHelper

/**
 *  自己的分享的文章
 */
class SharePrivateArticleHolder(
    private val slideHelper:SlideHelper,
    override val binding: ItemSharePrivateArticleBinding,
    private val deleteAction: (articleInfo: ArticleInfo?) -> Unit
) :
    BaseVHolder<ArticleInfo>(binding) {


    init {
        binding.root.setOnClickListener { v ->
            val bundle = Bundle()
            bundle.run {
                putInt("type", ArticleType.TYPE_LIST_ARTICLE)
                putInt("id", binding.articleInfo?.id ?: 0)
                putString("title", binding.articleInfo?.title )
                putString("author", binding.articleInfo?.author)
                putString("linkUrl", binding.articleInfo?.link)
                putBoolean("collect", binding.articleInfo?.collect ?: false)
            }
            v.findNav().navigate(R.id.fragment_web, bundle)
        }

        binding.slSlide.setOnStateChangeListener(object : SlideLayout.OnStateChangeListener() {
            override fun onInterceptTouchEvent(layout: SlideLayout): Boolean {
                val result: Boolean = slideHelper.closeAll(layout)
                return false
            }

            override fun onStateChanged(layout: SlideLayout, open: Boolean) {
                binding.articleInfo?.isOpen = open
                slideHelper.onStateChanged(layout, open)
            }
        })

        binding.tvDelete.setOnClickListener {
            if (!Settings.isLogined) {
                it.startLogin()
                return@setOnClickListener
            }
            deleteAction.invoke(binding.articleInfo)
        }
    }


    override fun bind(data: ArticleInfo?, position: Int) {
        binding.articleInfo = data
        binding.executePendingBindings()
    }
}