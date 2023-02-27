package com.wislie.common.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseVHolder<T>(open val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {

    abstract fun bind(data: T?, position: Int)
}