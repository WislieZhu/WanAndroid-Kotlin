package com.wislie.wanandroid.adapter.holder

import android.os.Bundle
import com.wislie.common.base.BaseVHolder
import com.wislie.common.ext.findNav
import com.wislie.wanandroid.R
import com.wislie.wanandroid.data.TreeInfo
import com.wislie.wanandroid.databinding.ItemTreeBinding

/**
 * 体系
 */
class TreeListHolder(
    override val binding: ItemTreeBinding
) :
    BaseVHolder<TreeInfo>(binding) {

    init {
        binding.flowlayoutSystem.setOnTagClickListener { v, position, parent ->
            val bundle = Bundle()
            binding.systemInfo?.children?.get(position)?.run {
                bundle.putInt("cid", id)
            }
            v.findNav().navigate(R.id.fragment_tree_article_list, bundle)
            true
        }
    }

    override fun bind(data: TreeInfo?, position: Int) {
        binding.systemInfo = data
        binding.executePendingBindings()
    }
}