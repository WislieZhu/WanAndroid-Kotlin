package com.wislie.wanandroid.adapter.holder

import android.os.Bundle
import com.d.lib.slidelayout.SlideLayout
import com.wislie.common.base.BaseVHolder
import com.wislie.common.ext.findNav
import com.wislie.wanandroid.R
import com.wislie.wanandroid.data.ArticleInfo
import com.wislie.wanandroid.databinding.ItemSharePrivateArticleBinding
import com.wislie.wanandroid.ext.startLogin
import com.wislie.wanandroid.util.*

/**
 *  自己的分享的文章
 */
class SharePrivateArticleHolder(
    private val slideHelper: SlideHelper,
    override val binding: ItemSharePrivateArticleBinding,
    private val deleteAction: (articleInfo: ArticleInfo?) -> Unit
) :
    BaseVHolder<ArticleInfo>(binding) {


    init {
        binding.root.setOnClickListener { v ->
            val bundle = Bundle()
            bundle.run {
                putInt(ARTICLE_TYPE, ArticleType.TYPE_LIST_ARTICLE)
                putInt(ARTICLE_ID, binding.articleInfo?.id ?: 0)
                putString(ARTICLE_TITLE, binding.articleInfo?.title )
                putString(ARTICLE_AUTHOR, binding.articleInfo?.author)
                putString(ARTICLE_LINK, binding.articleInfo?.link)
                putBoolean(ARTICLE_COLLECT, binding.articleInfo?.collect ?: false)
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
            if (!Settings.logined) {
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