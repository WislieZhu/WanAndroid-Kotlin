package com.wislie.wanandroid.adapter.holder

import android.os.Bundle
import com.wislie.common.base.BaseVHolder
import com.wislie.common.ext.findNav
import com.wislie.wanandroid.R
import com.wislie.wanandroid.data.VisitMostWeb
import com.wislie.wanandroid.databinding.ItemVisitMostWebBinding
import com.wislie.wanandroid.util.ArticleType

class VisitMostWebHolder(override val binding: ItemVisitMostWebBinding)
    :BaseVHolder<VisitMostWeb>(binding) {


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

    override fun bind(data: VisitMostWeb?, position: Int) {
        binding.webInfo = data
        binding.executePendingBindings()
    }
}