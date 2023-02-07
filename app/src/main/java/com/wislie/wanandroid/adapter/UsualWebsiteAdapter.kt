package com.wislie.wanandroid.adapter

import androidx.recyclerview.widget.DiffUtil
import com.wislie.common.base.BaseAdapter
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.holder.UsualWebsiteHolder
import com.wislie.wanandroid.data.UsualWebsite
import com.wislie.wanandroid.databinding.ItemUsualWebsiteBinding

class UsualWebsiteAdapter : BaseAdapter<UsualWebsite, ItemUsualWebsiteBinding, UsualWebsiteHolder>(
    callback
) {
    override fun onCreateViewHolder(binding: ItemUsualWebsiteBinding): UsualWebsiteHolder {
        return UsualWebsiteHolder(binding)
    }

    override fun getItemLayoutId(): Int {
        return R.layout.item_usual_website
    }

    companion object {
        val callback = object : DiffUtil.ItemCallback<UsualWebsite>() {

            override fun areItemsTheSame(oldItem: UsualWebsite, newItem: UsualWebsite): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: UsualWebsite, newItem: UsualWebsite): Boolean {
                return oldItem == newItem
            }
        }
    }
}