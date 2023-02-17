package com.wislie.wanandroid.adapter.holder

import android.os.Bundle
import com.wislie.common.base.BaseVHolder
import com.wislie.common.ext.findNav
import com.wislie.wanandroid.R
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

            val bundle = Bundle().apply {
                val todo = binding.todoInfo
                putInt("id", todo?.id ?: -1)
                putString("title", todo?.title)
                putString("content", todo?.content)
                putString("dateStr", todo?.dateStr)
                putInt("status", todo?.status ?: 0)
                putInt("type", todo?.type ?: 0)
                putInt("priority", todo?.priority?:0)
            }
            v.findNav().navigate(R.id.fragment_update_todo, bundle)
        }


    }


    override fun bind(data: ToDoInfo?, position: Int) {
        binding.todoInfo = data
        binding.executePendingBindings()
    }
}