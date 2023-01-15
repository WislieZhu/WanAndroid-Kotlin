package com.wislie.wanandroid.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.wislie.wanandroid.adapter.holder.LoadStateViewHolder

class LoadStateFooterAdapter(private val retry: () -> Unit):LoadStateAdapter<LoadStateViewHolder>() {
    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        Log.i("wislieZhu","bind---")
        holder.bindState(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        Log.i("wislieZhu","onCreate---")
        return LoadStateViewHolder(parent,retry)
    }
}