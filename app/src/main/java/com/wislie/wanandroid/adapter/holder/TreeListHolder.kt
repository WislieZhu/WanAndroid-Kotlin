package com.wislie.wanandroid.adapter.holder

import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
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
        binding.flowlayoutSystem.setOnTagClickListener { v, position, _ ->
            val bundle = Bundle()

            binding.systemInfo?.run {
                val systemInfo = this

                systemInfo.children?.get(position)?.run {
                    bundle.putInt("cid", id)
                    bundle.putString("title",systemInfo.name)
                    val gson = Gson()
                    val childrenStr = gson.toJson(systemInfo.children)
                    //转换成字符串
                    bundle.putString("treeInfo",childrenStr)
                }
            }

            v.findNav().navigate(R.id.fragment_tree_category, bundle)
            true
        }
    }

    override fun bind(data: TreeInfo?, position: Int) {
        binding.systemInfo = data
        binding.executePendingBindings()
    }
}