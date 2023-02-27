package com.wislie.wanandroid.adapter.holder

import android.os.Bundle
import com.wislie.common.base.BaseVHolder
import com.wislie.common.ext.findNav
import com.wislie.wanandroid.R
import com.wislie.wanandroid.data.NaviInfo
import com.wislie.wanandroid.databinding.ItemNaviBinding
import com.wislie.wanandroid.util.ArticleType

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
                    putInt("type", ArticleType.TYPE_LIST_ARTICLE)
                    putInt("id", articleInfo.id)
                    putString("title", articleInfo.title)
                    putString("author", articleInfo.author)
                    putString("linkUrl", articleInfo.link)
                    putBoolean("collect", articleInfo.collect)
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