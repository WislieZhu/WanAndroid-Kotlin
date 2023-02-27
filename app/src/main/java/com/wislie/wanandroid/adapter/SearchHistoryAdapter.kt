package com.wislie.wanandroid.adapter

import androidx.recyclerview.widget.DiffUtil
import com.wislie.common.base.BaseAdapter
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.holder.SearchHistoryHolder
import com.wislie.wanandroid.databinding.ItemSearchHistoryBinding
import com.wislie.wanandroid.db.SearchKey

/**
 * 搜索历史
 */
class SearchHistoryAdapter(
    private val clearClick: (SearchKey) -> Unit,
    private val itemClick: (SearchKey) -> Unit
) :
    BaseAdapter<SearchKey, ItemSearchHistoryBinding, SearchHistoryHolder>(
        callback
    ) {
    override fun onCreateViewHolder(binding: ItemSearchHistoryBinding): SearchHistoryHolder {
        return SearchHistoryHolder(binding, clearClick, itemClick)
    }

    override fun getItemLayoutId(): Int {
        return R.layout.item_search_history
    }

    companion object {
        val callback = object : DiffUtil.ItemCallback<SearchKey>() {

            override fun areItemsTheSame(oldItem: SearchKey, newItem: SearchKey): Boolean {
                return oldItem.hotKey == newItem.hotKey
            }

            override fun areContentsTheSame(oldItem: SearchKey, newItem: SearchKey): Boolean {
                return oldItem == newItem
            }
        }
    }
}







