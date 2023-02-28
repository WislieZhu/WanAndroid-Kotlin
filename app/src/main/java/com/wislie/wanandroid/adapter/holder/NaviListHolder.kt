package com.wislie.wanandroid.adapter.holder

import android.os.Bundle
import com.wislie.common.base.BaseVHolder
import com.wislie.common.ext.findNav
import com.wislie.wanandroid.R
import com.wislie.wanandroid.data.NaviInfo
import com.wislie.wanandroid.databinding.ItemNaviBinding
import com.wislie.wanandroid.util.*

/**
 * 导航
 */
class NaviListHolder(
    override val binding: ItemNaviBinding
) :
    BaseVHolder<NaviInfo>(binding) {

    init {
        binding.flowlayoutNavi.setOnTagClickListener { v, position, _ ->
            binding.naviInfo?.run {
                val naviInfo = this
                val articleInfo = naviInfo.articles[position]
                val bundle = Bundle().apply {
                    putInt(ARTICLE_TYPE, ArticleType.TYPE_LIST_ARTICLE)
                    putInt(ARTICLE_ID, articleInfo.id)
                    putString(ARTICLE_TITLE, articleInfo.title)
                    putString(ARTICLE_AUTHOR, articleInfo.author)
                    putString(ARTICLE_LINK, articleInfo.link)
                    putBoolean(ARTICLE_COLLECT, articleInfo.collect)
                }
                v.findNav().navigate(R.id.fragment_web, bundle)
            }
            true
        }
    }

    override fun bind(data: NaviInfo?, position: Int) {
        binding.naviInfo = data
        binding.executePendingBindings()
    }
}