package com.wislie.wanandroid.adapter

import androidx.recyclerview.widget.DiffUtil
import com.wislie.common.base.BaseAdapter
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.holder.TreeListHolder
import com.wislie.wanandroid.data.TreeInfo
import com.wislie.wanandroid.databinding.ItemTreeBinding

class TreeListAdapter: BaseAdapter<TreeInfo, ItemTreeBinding, TreeListHolder>(callback) {

    companion object {
        val callback = object : DiffUtil.ItemCallback<TreeInfo>() {

            override fun areItemsTheSame(oldItem: TreeInfo, newItem: TreeInfo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TreeInfo, newItem: TreeInfo): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemLayoutId(): Int {
        return R.layout.item_tree
    }

    override fun onCreateViewHolder(binding: ItemTreeBinding): TreeListHolder {
        return TreeListHolder(binding)
    }
}