package com.wislie.wanandroid.adapter.holder

import android.os.Bundle
import com.wislie.common.base.BaseVHolder
import com.wislie.common.ext.findNav
import com.wislie.wanandroid.R
import com.wislie.wanandroid.data.CollectWebsiteInfo
import com.wislie.wanandroid.databinding.ItemWebsiteBinding
import com.wislie.wanandroid.util.Settings
import com.wislie.wanandroid.ext.startLogin
import com.wislie.wanandroid.util.ArticleType

class CollectWebsiteHolder(
    override val binding: ItemWebsiteBinding,
    private val uncollect: (collectWebsiteInfo: CollectWebsiteInfo?) -> Unit
) :
    BaseVHolder<CollectWebsiteInfo>(binding) {


    init {
        //跳转到webFragment
        binding.root.setOnClickListener { v ->
            val bundle = Bundle()
            bundle.run {
                putInt("type", ArticleType.TYPE_WEBSITE)
                putInt("id", binding.websiteInfo?.id ?: 0)
                putString("title", binding.websiteInfo?.name ?: "")
                putString("linkUrl", binding.websiteInfo?.link ?: "")
                putBoolean("collect", true)
            }
            v.findNav().navigate(R.id.fragment_web, bundle)
        }

        binding.ivUncollect.setOnClickListener { // 在onBindViewHolder中点击不合理
            if (!Settings.logined) {
                it.startLogin()
                return@setOnClickListener
            }
            uncollect.invoke(binding.websiteInfo)
        }
    }


    override fun bind(data: CollectWebsiteInfo?, position: Int) {
        binding.websiteInfo = data
        binding.executePendingBindings()
    }
}