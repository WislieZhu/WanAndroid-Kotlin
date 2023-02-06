package com.wislie.wanandroid.fragment

import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import com.wislie.common.base.*
import com.wislie.common.ext.addFreshListener
import com.wislie.common.ext.findNav
import com.wislie.common.ext.init
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.LoadStateFooterAdapter
import com.wislie.wanandroid.adapter.VisitMostWebAdapter
import com.wislie.wanandroid.databinding.FragmentVisitMostWebBinding
import com.wislie.wanandroid.viewmodel.ArticlesViewModel
import kotlinx.coroutines.launch

/**
 * 访问最多的网站
 */
class VisitMostWebFragment : BaseViewModelFragment<BaseViewModel, FragmentVisitMostWebBinding>() {

    private val articlesViewModel: ArticlesViewModel by viewModels()

    private val adapter by lazy {
        VisitMostWebAdapter()
    }

    override fun init(root: View) {
        super.init(root)
        root.findViewById<Toolbar>(R.id.toolbar).run {
            setNavigationIcon(R.mipmap.ic_back)
            setBackgroundColor(ContextCompat.getColor(hostActivity, R.color.purple_500))
            title = "常用网站"
            setNavigationOnClickListener {
                findNav().navigateUp()
            }
        }

        registerLoadSir(binding.rvWeb) {
            adapter.refresh() //点击即刷新
        }
        binding.swipeRefreshLayout.init(adapter){
            if (binding.swipeRefreshLayout.isRefreshing) {
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
        binding.rvWeb.adapter =
            adapter.withLoadStateFooter(
                footer = LoadStateFooterAdapter(
                    retry = { adapter.retry() })
            )
        adapter.addFreshListener(mBaseLoadService)
    }

    override fun observeData() {
        super.observeData()
        articlesViewModel.visitMostWebLiveData
            .observe(viewLifecycleOwner) { resultState ->
                parseState(resultState, { dataList ->
                    if (binding.swipeRefreshLayout.isRefreshing) {
                        binding.swipeRefreshLayout.isRefreshing = false
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
        articlesViewModel.getVisitMostWeb()

    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_visit_most_web
    }
}