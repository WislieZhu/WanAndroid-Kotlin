package com.wislie.wanandroid.fragment

import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import com.wislie.common.base.BaseViewModel
import com.wislie.common.base.BaseViewModelFragment
import com.wislie.common.base.parseState
import com.wislie.common.base.parseStatexxx
import com.wislie.common.ext.addFreshListener
import com.wislie.common.ext.init
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.CollectWebsiteAdapter
import com.wislie.wanandroid.adapter.LoadStateFooterAdapter
import com.wislie.wanandroid.databinding.FragmentCollectWebsiteListBinding
import com.wislie.wanandroid.viewmodel.ArticlesViewModel
import kotlinx.coroutines.launch

/**
 * 收藏的网址列表
 */
class CollectWebsiteListFragment :
    BaseViewModelFragment<BaseViewModel, FragmentCollectWebsiteListBinding>() {


    private val articleViewModel: ArticlesViewModel by viewModels()

    private val adapter: CollectWebsiteAdapter by lazy {
        CollectWebsiteAdapter { collectWebsiteInfo ->
            collectWebsiteInfo?.run {
                articleViewModel.delCollectWebsite(this.id)
            }
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_collect_website_list
    }

    override fun init(root: View) {
        super.init(root)
        registerLoadSir(binding.rvWebsite) {
            loadData()
        }
        binding.swipeRefreshLayout.init(adapter) {
            loadData()
        }
        binding.rvWebsite.adapter =
            adapter.withLoadStateFooter(footer = LoadStateFooterAdapter { adapter.retry() })
        adapter.addFreshListener(mBaseLoadService)


    }

    override fun observeData() {
        super.observeData()
        articleViewModel.collectWebsitesLiveData
            .observe(viewLifecycleOwner) { resultState ->
                parseStatexxx(resultState) { websiteInfoList ->
                    if (binding.swipeRefreshLayout.isRefreshing) {
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                    lifecycleScope.launch {
                        websiteInfoList?.run {
                            val pagingData = PagingData.from(this)
                            Log.i("wislieZhu", "websiteInfoList size=${websiteInfoList?.size}")
                            adapter.submitData(lifecycle, pagingData)
                        }
                    }
                }
            }
        articleViewModel.delCollectWebsiteLiveData
            .observe(viewLifecycleOwner) { resultState ->
                parseState(resultState, { id -> //删除收藏成功
                    val list = adapter.snapshot().items
                    for (i in list.indices) {
                        if (list[i].id == id) {
                            adapter.notifyItemRemoved(i)
                        }
                    }
                })
            }
    }

    override fun loadData() {
        articleViewModel.getCollectWebsites()
    }
}