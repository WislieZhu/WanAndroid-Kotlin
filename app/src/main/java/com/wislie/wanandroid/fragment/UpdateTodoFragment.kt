package com.wislie.wanandroid.fragment

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.wislie.common.base.BaseViewModel
import com.wislie.common.base.BaseViewModelFragment
import com.wislie.common.base.parseState
import com.wislie.common.ext.findNav
import com.wislie.wanandroid.R
import com.wislie.wanandroid.databinding.FragmentTodoBinding
import com.wislie.wanandroid.viewmodel.TodoViewModel

/**
 * 修改todo
 */
class UpdateTodoFragment : BaseViewModelFragment<BaseViewModel, FragmentTodoBinding>() {

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
        arguments?.run {
            id = getInt("id")
            title = getString("title")
            content = getString("content")
            dateStr = getString("dateStr")
            status = getInt("status")
            type = getInt("type")
            priority = getInt("priority")
        }

        binding.tb.toolbar.run {
            setNavigationIcon(R.mipmap.ic_back)
            setBackgroundColor(ContextCompat.getColor(hostActivity, R.color.purple_500))
            title = "修改TODO"
            setNavigationOnClickListener {
                findNav().navigateUp()
            }
            inflateMenu(R.menu.add_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.item_add -> {
                        /* val direction =
                             CoinRankListFragmentDirections.actionFragmentCoinRankToFragmentMyCoin()
                         findNav()
                             .navigate(direction)*/
                    }
                }
                true
            }
        }

        binding.btnCommit.setOnClickListener {
            when{
                id == null -> return@setOnClickListener
                title.isNullOrEmpty() -> Toast.makeText(
                    hostActivity, "请输入标题",
                    Toast.LENGTH_SHORT
                ).show()
                content.isNullOrEmpty() -> Toast.makeText(
                    hostActivity, "请输入内容",
                    Toast.LENGTH_SHORT
                ).show()
                dateStr.isNullOrEmpty() -> Toast.makeText(
                    hostActivity, "请选择日期",
                    Toast.LENGTH_SHORT
                ).show() //todo 要修改
                priority == null -> Toast.makeText(
                    hostActivity, "请选择优先级",
                    Toast.LENGTH_SHORT
                ).show()
                else ->{
                    todoViewModel.updateTodo(id!!, title!!, content!!, dateStr!!, status, type, priority!!)
                }
            }
        }

    }

    override fun observeData() {
        super.observeData()
        todoViewModel.todoLiveData.observe(viewLifecycleOwner){
                resultState ->
            parseState(resultState, { todoInfo ->  //收藏成功
                Log.i("wislieZhu","todoInfo=$todoInfo")
            }, { errorMsg ->
            })
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_todo
    }
}