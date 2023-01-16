package com.wislie.wanandroid.adapter

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.wislie.wanandroid.adapter.holder.LoadStateViewHolder

class LoadStateFooterAdapter(private val retry: () -> Unit):LoadStateAdapter<LoadStateViewHolder>() {
    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bindState(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder(parent,retry)
    }

    override fun displayLoadStateAsItem(loadState: LoadState): Boolean {
        return super.displayLoadStateAsItem(loadState)
                || (loadState is LoadState.NotLoading && loadState.endOfPaginationReached)
    }
}