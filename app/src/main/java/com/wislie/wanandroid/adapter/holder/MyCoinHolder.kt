package com.wislie.wanandroid.adapter.holder

import com.wislie.common.base.BaseVHolder
import com.wislie.wanandroid.data.CoinItem
import com.wislie.wanandroid.databinding.ItemMyCoinBinding

class MyCoinHolder(
    override val binding: ItemMyCoinBinding,
) : BaseVHolder<CoinItem>(binding) {

    override fun bind(data: CoinItem?, position: Int) {
        binding.coinItem = data
        binding.executePendingBindings()
    }
}