package com.wislie.wanandroid.adapter

import androidx.recyclerview.widget.DiffUtil
import com.wislie.common.base.BaseAdapter
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.holder.MyCoinHolder
import com.wislie.wanandroid.data.CoinItem
import com.wislie.wanandroid.databinding.ItemMyCoinBinding

/**
 * 我的积分
 */
class MyCoinAdapter :
    BaseAdapter<CoinItem, ItemMyCoinBinding, MyCoinHolder>(
        MyCoinCallback()
    ) {
    override fun onCreateViewHolder(binding: ItemMyCoinBinding): MyCoinHolder {
        return MyCoinHolder(binding)
    }

    override fun getItemLayoutId(): Int {
        return R.layout.item_my_coin
    }
}

class MyCoinCallback: DiffUtil.ItemCallback<CoinItem>() {

    override fun areItemsTheSame(oldItem: CoinItem, newItem: CoinItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CoinItem, newItem: CoinItem): Boolean {
        return oldItem == newItem
    }
}







