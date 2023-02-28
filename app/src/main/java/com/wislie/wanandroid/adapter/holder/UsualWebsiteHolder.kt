package com.wislie.wanandroid.adapter.holder

import android.os.Bundle
import com.wislie.common.base.BaseVHolder
import com.wislie.common.ext.findNav
import com.wislie.wanandroid.R
import com.wislie.wanandroid.data.UsualWebsite
import com.wislie.wanandroid.databinding.ItemUsualWebsiteBinding
import com.wislie.wanandroid.util.*

class UsualWebsiteHolder(override val binding: ItemUsualWebsiteBinding) :
    BaseVHolder<UsualWebsite>(binding) {


    init {
        binding.root.setOnClickListener { v ->
            val bundle = Bundle().apply { //网址
                putInt(ARTICLE_TYPE, ArticleType.TYPE_WEBSITE)
                putInt(ARTICLE_ID, binding.webInfo?.id ?: 0)
                putString(ARTICLE_TITLE, binding.webInfo?.name ?: "")
                putString(ARTICLE_LINK, binding.webInfo?.link ?: "")
                putBoolean(ARTICLE_COLLECT, false)
            }

            v.findNav().navigate(R.id.fragment_web, bundle)
        }

    }

    override fun bind(data: UsualWebsite?, position: Int) {
        binding.webInfo = data
        binding.executePendingBindings()
    }
}