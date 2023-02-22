package com.wislie.wanandroid.fragment

import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.wislie.common.base.BaseViewModelFragment
import com.wislie.common.base.parseState
import com.wislie.common.ext.findNav
import com.wislie.wanandroid.App
import com.wislie.wanandroid.R
import com.wislie.wanandroid.databinding.FragmentTodoBinding
import com.wislie.wanandroid.ext.showCalendar
import com.wislie.wanandroid.viewmodel.TodoStateViewModel
import com.wislie.wanandroid.viewmodel.TodoViewModel

/**
 * 修改todo
 */
class TodoAddFragment : BaseViewModelFragment<TodoStateViewModel, FragmentTodoBinding>() {

    private val todoViewModel: TodoViewModel by viewModels()

    override fun init(root: View) {
        super.init(root)
        binding.todoStateVm = mViewModel

        binding.tb.toolbar.run {
            setNavigationIcon(R.mipmap.ic_back)
            setBackgroundColor(ContextCompat.getColor(hostActivity, R.color.purple_500))
            title = "新增TODO"
            setNavigationOnClickListener {
                findNav().navigateUp()
            }
        }

        binding.tvChooseDate.setOnClickListener { //选择日期
            showCalendar { year, month, day ->
                mViewModel?.date?.set("$year-$month-$day")
            }
        }

        binding.btnCommit.setOnClickListener {
            when {
                id == null -> return@setOnClickListener
                mViewModel?.title?.get().isNullOrEmpty() -> Toast.makeText(
                    hostActivity, "请输入标题",
                    Toast.LENGTH_SHORT
                ).show()
                mViewModel?.content?.get().isNullOrEmpty() -> Toast.makeText(
                    hostActivity, "请输入内容",
                    Toast.LENGTH_SHORT
                ).show()
                mViewModel?.date?.get().isNullOrEmpty() -> Toast.makeText(
                    hostActivity, "请选择日期",
                    Toast.LENGTH_SHORT
                ).show()
                mViewModel?.priority?.get() == null -> Toast.makeText(
                    hostActivity, "请选择优先级",
                    Toast.LENGTH_SHORT
                ).show()
                else -> {
                    todoViewModel.addTodo(
                        mViewModel?.title?.get()!!,
                        mViewModel?.content?.get()!!,
                        mViewModel?.date?.get()!!,
                        mViewModel?.type?.get() ?: 0,
                        mViewModel?.priority?.get()!!,
                    )
                }
            }
        }

    }

    override fun observeData() {
        super.observeData()
        todoViewModel.todoAddLiveData.observe(viewLifecycleOwner) { resultState ->
            parseState(resultState, { todoInfo ->
                App.instance().appViewModel.todoInfoAddLiveData.value = todoInfo
                findNav().navigateUp()
            })
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_todo
    }
}