package com.wislie.wanandroid.fragment

import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import com.wislie.common.base.BaseViewModel
import com.wislie.common.base.BaseViewModelFragment
import com.wislie.common.base.parseState
import com.wislie.common.ext.addFreshListener
import com.wislie.common.ext.init
import com.wislie.common.ext.showErrorCallback
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.LoadStateFooterAdapter
import com.wislie.wanandroid.adapter.TreeListAdapter
import com.wislie.wanandroid.databinding.FragmentTreeListBinding
import com.wislie.wanandroid.viewmodel.ArticlesViewModel
import kotlinx.coroutines.launch

/**
 * 体系列表
 */
class TreeListFragment : BaseViewModelFragment<BaseViewModel, FragmentTreeListBinding>() {

    private val articlesViewModel: ArticlesViewModel by viewModels()

    private val adapter: TreeListAdapter by lazy {
        TreeListAdapter()
    }

    override fun init(root: View) {
        super.init(root)
        registerLoadSir(binding.rvTree) {
            loadData()
        }
        binding.swipeRefreshLayout.init(adapter) {
            loadData()
        }
        binding.rvTree.adapter =
            adapter.withLoadStateFooter(
                footer = LoadStateFooterAdapter(
                    retry = { adapter.retry() })
            )
        adapter.addFreshListener(mBaseLoadService)
    }

    override fun loadData() {
        super.loadData()
        articlesViewModel.getTreeList()
    }

    override fun observeData() {
        super.observeData()
        articlesViewModel.treeListLiveData.observe(viewLifecycleOwner) { resultState ->
            parseState(resultState, { treeList ->
                treeList?.run {
                    //list 数据填充
                    lifecycleScope.launch {
                        val pagingData = PagingData.from(treeList)
                        adapter.submitData(pagingData)
                    }
                }
            }, { errorMsg ->
                mBaseLoadService.showErrorCallback()
            })
            if (binding.swipeRefreshLayout.isRefreshing) {
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_tree_list
    }
}