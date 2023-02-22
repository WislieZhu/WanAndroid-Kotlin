package com.wislie.wanandroid.adapter.holder

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.d.lib.slidelayout.SlideLayout
import com.d.lib.slidelayout.SlideLayout.OnStateChangeListener
import com.wislie.common.base.BaseVHolder
import com.wislie.common.ext.findNav
import com.wislie.wanandroid.R
import com.wislie.wanandroid.data.ToDoInfo
import com.wislie.wanandroid.databinding.ItemTodoBinding
import com.wislie.wanandroid.util.SlideHelper

/**
 * to do
 */
class TodoHolder(
    val slideHelper: SlideHelper,
    override val binding: ItemTodoBinding,
    onDeleteClick: (ToDoInfo?) -> Unit,
    onDoneClick: (ToDoInfo?) -> Unit
) : BaseVHolder<ToDoInfo>(binding) {


    private var index: Int = 0

    init {


        binding.slSlide.setOnStateChangeListener(object : OnStateChangeListener() {
            override fun onInterceptTouchEvent(layout: SlideLayout): Boolean {
                val result: Boolean = slideHelper.closeAll(layout)
                return false
            }

            override fun onStateChanged(layout: SlideLayout, open: Boolean) {
                binding.todoInfo?.isOpen = open
                slideHelper.onStateChanged(layout, open)
            }
        })
        binding.tvStick.setOnClickListener {
            binding.slSlide.setOpen(false, false)
            onDoneClick.invoke(binding.todoInfo)
        }
        binding.tvDelete.setOnClickListener { //删除
            binding.slSlide.close()
            onDeleteClick.invoke(binding.todoInfo)
        }
        binding.slSlide.setOnClickListener { v ->
            if (binding.slSlide.isOpen) {
                binding.slSlide.close()
                return@setOnClickListener
            }
            val bundle = Bundle().apply {
                val todo = binding.todoInfo
                putInt("id", todo?.id ?: -1)
                putString("title", todo?.title)
                putString("content", todo?.content)
                putString("dateStr", todo?.dateStr)
                putInt("status", todo?.status ?: 0)
                putInt("type", todo?.type ?: 0)
                putInt("priority", todo?.priority ?: 0)
            }
            v.findNav().navigate(R.id.fragment_update_todo, bundle)
        }

    }


    override fun bind(data: ToDoInfo?, position: Int) {
        data?.run {
            binding.slSlide.setOpen(this.isOpen, false)
        }
        binding.todoInfo = data
        index = position
        binding.executePendingBindings()
    }
}