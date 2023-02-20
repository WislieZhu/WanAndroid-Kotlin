package com.wislie.wanandroid.adapter.holder

import android.os.Bundle
import android.util.Log
import android.view.View
import com.wislie.common.base.BaseVHolder
import com.wislie.common.ext.findNav
import com.wislie.common.util.Utils
import com.wislie.wanandroid.R
import com.wislie.wanandroid.data.ToDoInfo
import com.wislie.wanandroid.databinding.ItemTodoBinding
import com.wislie.wanandroid.widget.LeftSlideView

/**
 * to do
 */
class TodoHolder(
    override val binding: ItemTodoBinding, onDeleteClick:(ToDoInfo?)->Unit
) :
    BaseVHolder<ToDoInfo>(binding), LeftSlideView.IonSlidingButtonListener  {

    private var mMenu:LeftSlideView? = null

    private var index:Int = 0

    init {

        binding.layoutContent.layoutParams.width =
            Utils.getScreenWidth(binding.root.context) - Utils.dp2px(binding.root.context, 20f)
        binding.layoutContent.setOnClickListener {v->
            //判断是否有删除菜单打开
            if (menuIsOpen()) {
                closeMenu()//关闭菜单
            } else {
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
        binding.tvDelete.setOnClickListener {v->
            Log.i("wislieZhu","onDeleteBtnCilck index=$index")
//            mIDeleteBtnClickListener.onDeleteBtnCilck(v, index)
            onDeleteClick.invoke(binding.todoInfo)
        }
        binding.leftSv.setSlidingButtonListener(this)
    }


    override fun bind(data: ToDoInfo?, position: Int) {
        binding.todoInfo = data
        index = position
        binding.executePendingBindings()
    }

    override fun onMenuIsOpen(view: View) {
        mMenu = view as LeftSlideView
    }

    override fun onDownOrMove(leftSlideView: LeftSlideView) {
        if (menuIsOpen()) {
            if (mMenu != leftSlideView) {
                closeMenu()
            }
        }
    }

    private fun menuIsOpen():Boolean{
        mMenu?.run {
            return true
        }
        return false
    }

    private fun closeMenu(){
        mMenu?.closeMenu()
        mMenu = null
    }
}