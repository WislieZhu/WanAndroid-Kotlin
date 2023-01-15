package com.wislie.wanandroid.adapter.holder

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.wislie.wanandroid.R
import com.wislie.wanandroid.databinding.ItemLoadStateBinding

class LoadStateViewHolder(parent: ViewGroup, var retry: () -> Unit) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_load_state, parent, false)
    ) {
    var binding: ItemLoadStateBinding = ItemLoadStateBinding.bind(itemView)
    fun bindState(loadState: LoadState) {
        when (loadState) {
            is LoadState.Error -> {
                binding.btnRetry.visibility = View.VISIBLE
                binding.btnRetry.setOnClickListener {
                    retry()
                }
                Log.d("wislieZhu", "Error了吧")
            }
            is LoadState.Loading -> {
                binding.llLoading.visibility = View.VISIBLE
                Log.d("wislieZhu", "Loading了吧")
            }
            else -> {
                Log.d("wislieZhu", "else了吧")
            }
        }
    }


}