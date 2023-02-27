package com.wislie.wanandroid.fragment

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import com.wislie.common.base.BaseViewModel
import com.wislie.common.base.BaseViewModelFragment
import com.wislie.common.base.parseState
import com.wislie.common.ext.addStateListener
import com.wislie.common.ext.init
import com.wislie.common.ext.showErrorCallback
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.TreeListAdapter
import com.wislie.wanandroid.databinding.FragmentListBinding
import com.wislie.wanandroid.viewmodel.ArticlesViewModel
import kotlinx.coroutines.launch

/**
 * 体系列表
 * 两种不同类型的数据
 *
 */
class TreeListFragment : BaseViewModelFragment<BaseViewModel, FragmentListBinding>() {

    private val articlesViewModel: ArticlesViewModel by viewModels()

    private val adapter: TreeListAdapter by lazy {
        TreeListAdapter()
    }

    override fun init(root: View) {
        super.init(root)
        registerLoadSir(binding.list.swipeRv) {
            adapter.refresh() //点击即刷新
        }
        binding.list.swipeRefreshLayout.init {
            adapter.refresh() //点击即刷新
        }
        binding.list.swipeRv.adapter = adapter
        adapter.addStateListener(hostActivity, mBaseLoadService)
    }

    override fun loadData() {
        super.loadData()
        articlesViewModel.getTreeList()
    }

    override fun observeData() {
        super.observeData()
        articlesViewModel.treeListLiveData.observe(viewLifecycleOwner) { resultState ->
            if (binding.list.swipeRefreshLayout.isRefreshing) {
                binding.list.swipeRefreshLayout.isRefreshing = false
            }
            parseState(resultState, { treeList ->
                treeList?.run {
                    //list 数据填充
                    lifecycleScope.launch {
                        val pagingData = PagingData.from(treeList)
                        adapter.submitData(pagingData)
                    }
                }
            }, {
                mBaseLoadService.showErrorCallback()
            })
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_list
    }
}