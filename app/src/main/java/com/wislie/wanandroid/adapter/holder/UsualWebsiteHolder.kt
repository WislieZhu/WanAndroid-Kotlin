package com.wislie.wanandroid.adapter.holder

import android.os.Bundle
import com.wislie.common.base.BaseVHolder
import com.wislie.common.ext.findNav
import com.wislie.wanandroid.R
import com.wislie.wanandroid.data.UsualWebsite
import com.wislie.wanandroid.databinding.ItemUsualWebsiteBinding
import com.wislie.wanandroid.util.ArticleType

class UsualWebsiteHolder(override val binding: ItemUsualWebsiteBinding)
    :BaseVHolder<UsualWebsite>(binding) {


    init {
        binding.root.setOnClickListener { v->
            val bundle = Bundle()

            bundle.putInt("type", ArticleType.TYPE_WEBSITE) //网址
            bundle.putInt("id",  binding.webInfo?.id?:0)
            bundle.putString("linkUrl",  binding.webInfo?.link ?: "")
            bundle.putString("name",  binding.webInfo?.name ?: "")
            v.findNav().navigate(R.id.fragment_web, bundle)
        }

    }

    override fun bind(data: UsualWebsite?, position: Int) {
        binding.webInfo = data
        binding.executePendingBindings()
    }
}