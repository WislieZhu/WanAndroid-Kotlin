package com.wislie.wanandroid.adapter

import androidx.recyclerview.widget.DiffUtil
import com.wislie.common.base.BaseAdapter
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.holder.TodoHolder
import com.wislie.wanandroid.data.ToDoInfo
import com.wislie.wanandroid.databinding.ItemTodoBinding
import com.wislie.wanandroid.util.SlideHelper
import com.wislie.wanandroid.util.SlideHelpera

/**
 * to do 适配器
 */
class TodoAdapter(
    private val onDeleteClick: (ToDoInfo?) -> Unit,
    private val onDoneClick: (ToDoInfo?) -> Unit
) :
    BaseAdapter<ToDoInfo, ItemTodoBinding, TodoHolder>(callback) {

    private val mSlideHelper by lazy {
        SlideHelper()
    }

    override fun onCreateViewHolder(binding: ItemTodoBinding): TodoHolder {
        return TodoHolder(mSlideHelper, binding, onDeleteClick, onDoneClick)
    }

    override fun getItemLayoutId(): Int {
        return R.layout.item_todo
    }

    companion object {
        val callback = object : DiffUtil.ItemCallback<ToDoInfo>() {

            override fun areItemsTheSame(oldItem: ToDoInfo, newItem: ToDoInfo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ToDoInfo, newItem: ToDoInfo): Boolean {
                return oldItem == newItem
            }
        }
    }
}







