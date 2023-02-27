package com.wislie.wanandroid.adapter.holder

import com.wislie.common.base.BaseVHolder
import com.wislie.wanandroid.databinding.ItemSearchHistoryBinding
import com.wislie.wanandroid.db.SearchKey

class SearchHistoryHolder(
    override val binding: ItemSearchHistoryBinding,
    clearClick: (SearchKey) -> Unit,
    itemClick: (SearchKey) -> Unit
) : BaseVHolder<SearchKey>(binding) {


    init {
        binding.ivClear.setOnClickListener {
            binding.searchKey?.run {
                clearClick.invoke(this)
            }
        }
        binding.root.setOnClickListener {
            binding.searchKey?.run {
                itemClick.invoke(this)
            }
        }
    }

    override fun bind(data: SearchKey?, position: Int) {
        binding.searchKey = data
        binding.executePendingBindings()
    }
}