package com.wislie.wanandroid.adapter.holder

import com.wislie.common.base.BaseVHolder
import com.wislie.wanandroid.data.CollectWebsiteInfo
import com.wislie.wanandroid.databinding.ItemWebsiteBinding
import com.wislie.wanandroid.util.Settings
import com.wislie.wanandroid.ext.startLogin

class CollectWebsiteHolder(
    override val binding: ItemWebsiteBinding,
    val uncollect: (collectWebsiteInfo: CollectWebsiteInfo?) -> Unit
) :
    BaseVHolder<CollectWebsiteInfo>(binding) {


    init {
        binding.root.setOnClickListener { v ->


        }

        binding.ivUncollect.setOnClickListener { // 在onBindViewHolder中点击不合理
            if (!Settings.isLogined) {
                it.startLogin()
                return@setOnClickListener
            }
            uncollect.invoke( binding.websiteInfo)
        }
    }


    override fun bind(data: CollectWebsiteInfo?, position: Int) {
        binding.websiteInfo = data
        binding.executePendingBindings()
    }
}