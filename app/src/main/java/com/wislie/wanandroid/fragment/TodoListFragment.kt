package com.wislie.wanandroid.fragment

import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.TerminalSeparatorType
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.insertFooterItem
import com.wislie.common.base.BaseViewModel
import com.wislie.common.base.BaseViewModelFragment
import com.wislie.common.base.parseState
import com.wislie.common.ext.addFreshListener
import com.wislie.common.ext.findNav
import com.wislie.common.ext.init
import com.wislie.common.ext.showEmptyCallback
import com.wislie.wanandroid.App
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.LoadStateFooterAdapter
import com.wislie.wanandroid.adapter.TodoAdapter
import com.wislie.wanandroid.databinding.FragmentToolbarListBinding
import com.wislie.wanandroid.viewmodel.TodoViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

/**
 * todo列表
 */
class TodoListFragment : BaseViewModelFragment<BaseViewModel, FragmentToolbarListBinding>() {

    private val todoViewModel: TodoViewModel by viewModels()
    private val adapter: TodoAdapter by lazy {
        TodoAdapter({ toDoInfo -> //删除
            toDoInfo?.run {
                todoViewModel.deleteTodo(toDoInfo.id)
            }
        }, { toDoInfo -> //完成
            toDoInfo?.run {
                todoViewModel.doneTodo(toDoInfo.id)
            }
        })

    }

    override fun init(root: View) {
        super.init(root)

        binding.tb.toolbar.apply {
            setBackgroundColor(ContextCompat.getColor(hostActivity, R.color.purple_500))
            setNavigationIcon(R.mipmap.ic_back)
            title = "玩Android"
            inflateMenu(R.menu.add_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.item_add -> { //添加
                        findNav().navigate(R.id.fragment_add_todo)
                    }
                }
                true
            }
            setNavigationOnClickListener {
                findNav().navigateUp()
            }
        }

        registerLoadSir(binding.list.swipeRv) {
            adapter.refresh() //点击即刷新
        }
        binding.list.swipeRefreshLayout.init(adapter) {
            adapter.refresh() //点击即刷新
        }
        binding.list.swipeRv.adapter =
            adapter.withLoadStateFooter(
                footer = LoadStateFooterAdapter(
                    retry = { adapter.retry() })
            )
        adapter.addFreshListener(mBaseLoadService)
    }

    override fun loadData() {
        lifecycleScope.launch {
            todoViewModel
                .todoList
                .cachedIn(scope = lifecycleScope)
                .combine(todoViewModel.mRemovedFlow) { pagingData, removedList ->
                    pagingData.filter {
                        it !in removedList
                    }
                }
                .collectLatest {
                    if (binding.list.swipeRefreshLayout.isRefreshing) {
                        binding.list.swipeRefreshLayout.isRefreshing = false
                    }
                    adapter.submitData(lifecycle, it)
                }
        }
    }

    override fun observeData() {
        super.observeData()
        App.instance().appViewModel.todoInfoUpdateLiveData
            .observe(viewLifecycleOwner) {
                var list = adapter.snapshot().items
                val todoInfo = App.instance().appViewModel.todoInfoUpdateLiveData.value
                for (i in list.indices) {
                    if (list[i].id == todoInfo?.id) {
                        list[i].title = todoInfo.title
                        list[i].content = todoInfo.content
                        list[i].dateStr = todoInfo.dateStr
                        list[i].priority = todoInfo.priority
                        list[i].type = todoInfo.type
                        adapter.notifyItemChanged(i, Any())
                        break
                    }
                }
            }

        //新增Todo信息
        App.instance().appViewModel.todoInfoAddLiveData
            .observe(viewLifecycleOwner) { todoInfo ->
                loadData()
                //todo 总觉得这么写有问题，比如数量超过一页的时候
//                lifecycleScope.launch {
//                    todoViewModel
//                        .todoList
//                        .collectLatest {
//                            it.insertFooterItem(TerminalSeparatorType.FULLY_COMPLETE, todoInfo)
////                            adapter.submitData(lifecycle, it)
//                        }
//                }
            }

        //删除todo
        todoViewModel.todoDelLiveData.observe(viewLifecycleOwner) { resultState ->
            parseState(resultState, { id ->
                val list = adapter.snapshot().items
                for (i in list.indices) {
                    if (list[i].id == id) {
                        if (list.size == 1) {
                            mBaseLoadService.showEmptyCallback()
                        }
                        todoViewModel.removeFlowItem(list[i])
                        break
                    }
                }
            }, { errorMsg ->
            })
        }
        //完成
        todoViewModel.todoDoneLiveData.observe(viewLifecycleOwner){resultState ->
            parseState(resultState, { todoInfo ->
                val list = adapter.snapshot().items
                for (i in list.indices) {
                    if (list[i].id == todoInfo.id) {
                        list[i].status = 1
                        adapter.notifyItemChanged(i, Any())
                        break
                    }
                }
            }, { errorMsg ->
            })

        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_toolbar_list
    }
}