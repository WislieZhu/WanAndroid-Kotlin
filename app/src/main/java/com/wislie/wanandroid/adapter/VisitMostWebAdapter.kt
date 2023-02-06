package com.wislie.wanandroid.adapter

import androidx.recyclerview.widget.DiffUtil
import com.wislie.common.base.BaseAdapter
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.holder.VisitMostWebHolder
import com.wislie.wanandroid.data.VisitMostWeb
import com.wislie.wanandroid.databinding.ItemVisitMostWebBinding

class VisitMostWebAdapter : BaseAdapter<VisitMostWeb, ItemVisitMostWebBinding, VisitMostWebHolder>(
    callback
) {
    override fun onCreateViewHolder(binding: ItemVisitMostWebBinding): VisitMostWebHolder {
        return VisitMostWebHolder(binding)
    }

    override fun getItemLayoutId(): Int {
        return R.layout.item_visit_most_web
    }

    companion object {
        val callback = object : DiffUtil.ItemCallback<VisitMostWeb>() {

            override fun areItemsTheSame(oldItem: VisitMostWeb, newItem: VisitMostWeb): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: VisitMostWeb, newItem: VisitMostWeb): Boolean {
                return oldItem == newItem
            }
        }
    }
}