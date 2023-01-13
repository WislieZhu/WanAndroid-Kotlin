package com.wislie.wanandroid.adapter.holder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.wislie.wanandroid.R
import com.wislie.wanandroid.databinding.ItemLoadStateBinding

class LoadStateViewHolder (parent:ViewGroup, var retry:()->Unit ) :
    RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_load_state, parent,false)) {


    var binding :ItemLoadStateBinding = ItemLoadStateBinding.bind(itemView)

    fun bindState(loadState: LoadState){
        if(loadState is LoadState.Error){
            binding.btnRetry.visibility = View.VISIBLE
            binding.btnRetry.setOnClickListener {
                retry()
            }
        }else if(loadState is LoadState.Loading){
            binding.llLoading.visibility = View.VISIBLE
        }
    }



}