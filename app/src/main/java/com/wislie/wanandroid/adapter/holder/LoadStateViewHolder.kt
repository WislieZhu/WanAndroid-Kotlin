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
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_load_state, parent, false)
    ) {
    var binding: ItemLoadStateBinding = ItemLoadStateBinding.bind(itemView)

    init {
        binding.btnRetry.setOnClickListener {
            retry()
        }
    }

    fun bindState(loadState: LoadState) {
        when (loadState) {
            is LoadState.Error -> {
                Log.i("wislieZhu", "LoadStateViewHolder error..")
                binding.tvNoMoreData.visibility = View.GONE
                binding.btnRetry.visibility = View.VISIBLE
                binding.llLoading.visibility = View.GONE
            }
            is LoadState.Loading -> {
                Log.i("wislieZhu", "LoadStateViewHolder loading..")
                binding.tvNoMoreData.visibility = View.GONE
                binding.btnRetry.visibility = View.GONE
                binding.llLoading.visibility = View.VISIBLE
            }
            is LoadState.NotLoading -> {
                Log.i("wislieZhu", "LoadStateViewHolder notLoading..")
                if (loadState.endOfPaginationReached) {
                    Log.i("wislieZhu", "endOfPaginationReached..")
                    binding.tvNoMoreData.visibility = View.VISIBLE
                    binding.btnRetry.visibility = View.GONE
                    binding.llLoading.visibility = View.GONE
                } else {
                    Log.i("wislieZhu", "endOfPaginationReached..xx")
                    binding.tvNoMoreData.visibility = View.GONE
                    binding.btnRetry.visibility = View.GONE
                    binding.llLoading.visibility = View.GONE
                }
            }
        }
    }
}