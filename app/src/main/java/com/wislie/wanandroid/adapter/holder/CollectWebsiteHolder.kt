package com.wislie.wanandroid.adapter.holder

import android.os.Bundle
import com.wislie.common.base.BaseVHolder
import com.wislie.common.ext.findNav
import com.wislie.wanandroid.R
import com.wislie.wanandroid.data.CollectWebsiteInfo
import com.wislie.wanandroid.databinding.ItemWebsiteBinding
import com.wislie.wanandroid.ext.startLogin
import com.wislie.wanandroid.util.*

class CollectWebsiteHolder(
    override val binding: ItemWebsiteBinding,
    private val unCollect: (collectWebsiteInfo: CollectWebsiteInfo?) -> Unit
) :
    BaseVHolder<CollectWebsiteInfo>(binding) {


    init {
        //跳转到webFragment
        binding.root.setOnClickListener { v ->
            val bundle = Bundle()
            bundle.run {
                putInt(ARTICLE_TYPE, ArticleType.TYPE_WEBSITE)
                putInt(ARTICLE_ID, binding.websiteInfo?.id ?: 0)
                putString(ARTICLE_TITLE, binding.websiteInfo?.name ?: "")
                putString(ARTICLE_LINK, binding.websiteInfo?.link ?: "")
                putBoolean(ARTICLE_COLLECT, true)
            }
            v.findNav().navigate(R.id.fragment_web, bundle)
        }

        binding.ivUncollect.setOnClickListener { // 在onBindViewHolder中点击不合理
            if (!Settings.logined) {
                it.startLogin()
                return@setOnClickListener
            }
            unCollect.invoke(binding.websiteInfo)
        }
    }


    override fun bind(data: CollectWebsiteInfo?, position: Int) {
        binding.websiteInfo = data
        binding.executePendingBindings()
    }
}