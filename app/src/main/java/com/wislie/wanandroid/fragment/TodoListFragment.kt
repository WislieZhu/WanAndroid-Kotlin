package com.wislie.wanandroid.fragment

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.wislie.common.base.BaseViewModel
import com.wislie.common.base.BaseViewModelFragment
import com.wislie.common.ext.addFreshListener
import com.wislie.common.ext.findNav
import com.wislie.common.ext.init
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.LoadStateFooterAdapter
import com.wislie.wanandroid.adapter.TodoAdapter
import com.wislie.wanandroid.databinding.FragmentTodoListBinding
import com.wislie.wanandroid.viewmodel.TodoViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * todo列表
 */
class TodoListFragment : BaseViewModelFragment<BaseViewModel, FragmentTodoListBinding>() {

    private val todoViewModel: TodoViewModel by viewModels()
    private val adapter: TodoAdapter by lazy {
        TodoAdapter()
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
                .collectLatest {
                    if (binding.list.swipeRefreshLayout.isRefreshing) {
                        binding.list.swipeRefreshLayout.isRefreshing = false
                    }
                    adapter.submitData(lifecycle, it)
                }
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_todo_list
    }
}