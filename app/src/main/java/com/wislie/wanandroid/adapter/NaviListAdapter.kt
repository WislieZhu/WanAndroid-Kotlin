package com.wislie.wanandroid.adapter

import androidx.recyclerview.widget.DiffUtil
import com.wislie.common.base.BaseAdapter
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.holder.NaviListHolder
import com.wislie.wanandroid.data.NaviInfo
import com.wislie.wanandroid.databinding.ItemNaviBinding

class NaviListAdapter: BaseAdapter<NaviInfo, ItemNaviBinding, NaviListHolder>(callback) {

    companion object {
        val callback = object : DiffUtil.ItemCallback<NaviInfo>() {

            override fun areItemsTheSame(oldItem: NaviInfo, newItem: NaviInfo): Boolean {
                return oldItem.cid == newItem.cid
            }

            override fun areContentsTheSame(oldItem: NaviInfo, newItem: NaviInfo): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemLayoutId(): Int {
        return R.layout.item_navi
    }

    override fun onCreateViewHolder(binding: ItemNaviBinding): NaviListHolder {
        return NaviListHolder(binding)
    }
}