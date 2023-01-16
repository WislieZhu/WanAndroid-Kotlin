package com.wislie.wanandroid.adapter.holder

import com.wislie.common.base.BaseVHolder
import com.wislie.wanandroid.data.CoinRankInfo
import com.wislie.wanandroid.databinding.ItemCoinRankBinding

class CoinRankHolder(
    override val binding: ItemCoinRankBinding,
) : BaseVHolder<CoinRankInfo>(binding) {

    override fun bind(data: CoinRankInfo?, position: Int) {
        data?.index = position + 1
        binding.coinRankInfo = data
        binding.executePendingBindings()
    }
}