package com.wislie.wanandroid.adapter.holder

import com.wislie.common.base.BaseVHolder
import com.wislie.wanandroid.data.ToDoInfo
import com.wislie.wanandroid.databinding.ItemTodoBinding

/**
 * to do
 */
class TodoHolder(
    override val binding: ItemTodoBinding
) :
    BaseVHolder<ToDoInfo>(binding) {

    init {
        binding.root.setOnClickListener { v ->

        }


    }


    override fun bind(data: ToDoInfo?, position: Int) {
        binding.todoInfo = data
        binding.executePendingBindings()
    }
}