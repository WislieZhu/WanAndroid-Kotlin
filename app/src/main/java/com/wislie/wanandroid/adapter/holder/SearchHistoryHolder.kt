package com.wislie.wanandroid.adapter.holder

import com.wislie.common.base.BaseVHolder
import com.wislie.wanandroid.databinding.ItemSearchHistoryBinding
import com.wislie.wanandroid.db.SearchKey

class SearchHistoryHolder(
    override val binding: ItemSearchHistoryBinding, clearMethod: (SearchKey) -> Unit
) : BaseVHolder<SearchKey>(binding) {

    var index = 0

    init {
        binding.ivClear.setOnClickListener {
            binding.searchKey?.run {
                clearMethod.invoke(this)
            }
        }
    }

    override fun bind(data: SearchKey?, position: Int) {
        index = position
        binding.searchKey = data
        binding.executePendingBindings()
    }
}