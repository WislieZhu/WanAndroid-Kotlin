package com.wislie.wanandroid.adapter

import androidx.recyclerview.widget.DiffUtil
import com.wislie.common.base.BaseAdapter
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.holder.CoinRankHolder
import com.wislie.wanandroid.adapter.holder.MyCoinHolder
import com.wislie.wanandroid.data.CoinItem
import com.wislie.wanandroid.data.CoinRankInfo
import com.wislie.wanandroid.databinding.ItemCoinRankBinding
import com.wislie.wanandroid.databinding.ItemMyCoinBinding

/**
 * 积分排行榜
 */
class CoinRankAdapter :
    BaseAdapter<CoinRankInfo, ItemCoinRankBinding, CoinRankHolder>(
        CoinRankCallback()
    ) {
    override fun onCreateViewHolder(binding: ItemCoinRankBinding): CoinRankHolder {
        return CoinRankHolder(binding)
    }

    override fun getItemLayoutId(): Int {
        return R.layout.item_coin_rank
    }
}

class CoinRankCallback: DiffUtil.ItemCallback<CoinRankInfo>() {

    override fun areItemsTheSame(oldItem: CoinRankInfo, newItem: CoinRankInfo): Boolean {
        return oldItem.userId == newItem.userId
    }

    override fun areContentsTheSame(oldItem: CoinRankInfo, newItem: CoinRankInfo): Boolean {
        return oldItem == newItem
    }
}







