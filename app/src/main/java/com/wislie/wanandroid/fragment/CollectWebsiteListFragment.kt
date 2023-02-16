package com.wislie.wanandroid.fragment

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.paging.filter
import com.wislie.common.base.BaseViewModel
import com.wislie.common.base.BaseViewModelFragment
import com.wislie.common.base.parseState
import com.wislie.common.base.parseStateNoLogin
import com.wislie.common.ext.addFreshListener
import com.wislie.common.ext.init
import com.wislie.wanandroid.App
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.CollectWebsiteAdapter
import com.wislie.wanandroid.databinding.FragmentCollectWebsiteListBinding
import com.wislie.wanandroid.viewmodel.ArticlesViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * 收藏的网址列表
 */
class CollectWebsiteListFragment :
    BaseViewModelFragment<BaseViewModel, FragmentCollectWebsiteListBinding>() {


    private val articlesViewModel: ArticlesViewModel by viewModels()

    private val adapter: CollectWebsiteAdapter by lazy {
        CollectWebsiteAdapter { collectWebsiteInfo ->
            collectWebsiteInfo?.run {
                articlesViewModel.delCollectWebsite(collectWebsiteInfo.id)
            }
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_collect_website_list
    }

    override fun init(root: View) {
        super.init(root)
        registerLoadSir(binding.rvWebsite) {
            adapter.refresh() //点击即刷新
        }
        binding.swipeRefreshLayout.init(adapter) {
            adapter.refresh() //点击即刷新
        }
        binding.rvWebsite.adapter = adapter
        adapter.addFreshListener(mBaseLoadService)
    }

    override fun observeData() {
        super.observeData()
        //收藏
        articlesViewModel.collectWebsiteListLiveData
            .observe(viewLifecycleOwner) { resultState ->
                parseStateNoLogin(resultState) { websiteInfoList ->
                    if (binding.swipeRefreshLayout.isRefreshing) {
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                    lifecycleScope.launch {
                        websiteInfoList?.run {
                            //list转换成 Flow, 方便删除
                            val flow = MutableStateFlow(PagingData.from(this))
                            flow.combine(articlesViewModel.mRemovedFlow) { pagingData, removedList ->
                                pagingData.filter {
                                    it !in removedList
                                }
                            }.collectLatest {
                                adapter.submitData(lifecycle, it)
                            }
                        }
                    }
                }
            }

        //取消收藏
        articlesViewModel.delCollectWebsiteLiveData
            .observe(viewLifecycleOwner) { resultState ->
                parseState(resultState, { id ->
                    val list = adapter.snapshot().items
                    for (i in list.indices) {
                        if (list[i].id == id) {
                            articlesViewModel.removeFlowItem(list[i])
                        }
                    }
                }, { errorMsg ->
                })
            }

        //全局性质的取消收藏
        App.instance().appViewModel.collectEventLiveData
            .observe(viewLifecycleOwner, Observer { collectEvent ->
                val list = adapter.snapshot().items
                for (i in list.indices) {
                    if (list[i].id == collectEvent.id) {
                        articlesViewModel.removeFlowItem(list[i])
                        return@Observer
                    }
                }
                loadData()
            })
    }

    override fun loadData() {
        articlesViewModel.getCollectWebsiteList()
    }
}