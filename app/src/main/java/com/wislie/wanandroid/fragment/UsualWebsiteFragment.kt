package com.wislie.wanandroid.fragment

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import com.wislie.common.base.*
import com.wislie.common.ext.addStateListener
import com.wislie.common.ext.findNav
import com.wislie.common.ext.init
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.LoadStateFooterAdapter
import com.wislie.wanandroid.adapter.UsualWebsiteAdapter
import com.wislie.wanandroid.databinding.FragmentToolbarListBinding
import com.wislie.wanandroid.viewmodel.ArticlesViewModel
import kotlinx.coroutines.launch

/**
 * 常用网站
 */
class UsualWebsiteFragment : BaseViewModelFragment<BaseViewModel, FragmentToolbarListBinding>() {

    private val articlesViewModel: ArticlesViewModel by viewModels()

    private val adapter by lazy {
        UsualWebsiteAdapter()
    }

    override fun init(root: View) {
        super.init(root)
        binding.tb.toolbar.run {
            setNavigationIcon(R.mipmap.ic_back)
            setBackgroundColor(ContextCompat.getColor(hostActivity, R.color.purple_500))
            title = "常用网站"
            setTitleTextColor(ContextCompat.getColor(hostActivity, R.color.white))
            setNavigationOnClickListener {
                findNav().navigateUp()
            }
        }

        registerLoadSir(binding.list.swipeRv) {
            adapter.refresh() //点击即刷新
        }
        binding.list.swipeRefreshLayout.init{
            adapter.refresh() //点击即刷新
        }
        binding.list.swipeRv.adapter =
            adapter.withLoadStateFooter(
                footer = LoadStateFooterAdapter(
                    retry = { adapter.retry() })
            )
        adapter.addStateListener(hostActivity, mBaseLoadService)
    }

    override fun observeData() {
        super.observeData()
        articlesViewModel
            .usualWebsiteLiveData
            .observe(viewLifecycleOwner) { resultState ->
                parseState(resultState, { dataList ->
                    if (binding.list.swipeRefreshLayout.isRefreshing) {
                        binding.list.swipeRefreshLayout.isRefreshing = false
                    }
                    dataList?.run {
                        lifecycleScope.launch {
                            val pagingData = PagingData.from(dataList)
                            adapter.submitData(pagingData)
                        }
                    }
                })
            }
    }

    override fun loadData() {
        super.loadData()
        articlesViewModel.getUsualWebsite()
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_toolbar_list
    }
}