package com.wislie.wanandroid.adapter

import androidx.recyclerview.widget.DiffUtil
import com.wislie.common.base.BaseAdapter
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.holder.CollectWebsiteHolder
import com.wislie.wanandroid.data.CollectWebsiteInfo
import com.wislie.wanandroid.databinding.ItemWebsiteBinding

/**
 * 收藏的网址
 */
class CollectWebsiteAdapter(val collect: ( collectWebsiteInfo: CollectWebsiteInfo?) -> Unit) :
    BaseAdapter<CollectWebsiteInfo, ItemWebsiteBinding, CollectWebsiteHolder>(
        CollectWebsiteCallback()
    ) {
    override fun onCreateViewHolder(binding: ItemWebsiteBinding): CollectWebsiteHolder {
        return CollectWebsiteHolder(binding, collect)
    }

    override fun getItemLayoutId(): Int {
        return R.layout.item_website
    }
}
class CollectWebsiteCallback: DiffUtil.ItemCallback<CollectWebsiteInfo>() {

    override fun areItemsTheSame(oldItem: CollectWebsiteInfo, newItem: CollectWebsiteInfo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CollectWebsiteInfo, newItem: CollectWebsiteInfo): Boolean {
        return oldItem == newItem
    }
}






