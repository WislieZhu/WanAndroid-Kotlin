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
class TodoUpdateFragment : BaseViewModelFragment<TodoStateViewModel, FragmentTodoBinding>() {

    private val todoViewModel: TodoViewModel by viewModels()
    private var id: Int? = null
    private var title: String? = null
    private var content: String? = null
    private var dateStr: String? = null
    private var status: Int = 0 //默认未完成
    private var type: Int = 0
    private var priority: Int? = null

    override fun init(root: View) {
        super.init(root)
        binding.todoStateVm = mViewModel
        arguments?.run {
            id = getInt("id")
            title = getString("title")
            content = getString("content")
            dateStr = getString("dateStr")
            status = getInt("status")
            type = getInt("type")
            priority = getInt("priority")
        }

        title?.run {
            mViewModel?.title?.set(this)
        }

        content?.run {
            mViewModel?.content?.set(this)
        }

        dateStr?.run {
            mViewModel?.date?.set(this)
        }

        type?.run {
            mViewModel?.type?.set(this)
        }

        priority?.run {
            mViewModel?.priority?.set(this)
        }

        binding.tb.toolbar.run {
            setNavigationIcon(R.mipmap.ic_back)
            setBackgroundColor(ContextCompat.getColor(hostActivity, R.color.purple_500))
            title = "修改TODO"
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
                    todoViewModel.updateTodo(
                        id!!,
                        mViewModel?.title?.get()!!,
                        mViewModel?.content?.get()!!,
                        mViewModel?.date?.get()!!,
                        status,
                        mViewModel?.type?.get() ?: 0,
                        mViewModel?.priority?.get()?:0,
                    )
                }
            }
        }

    }

    override fun observeData() {
        super.observeData()
        todoViewModel.todoUpdateLiveData.observe(viewLifecycleOwner) { resultState ->
            parseState(resultState, { todoInfo ->
                App.instance().appViewModel.todoInfoUpdateLiveData.value = todoInfo
                findNav().navigateUp()
            }, { errorMsg ->


            })
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_todo
    }
}