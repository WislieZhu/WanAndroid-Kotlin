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
import com.wislie.wanandroid.adapter.MyCoinAdapter
import com.wislie.wanandroid.databinding.FragmentToolbarListBinding
import com.wislie.wanandroid.ext.initFab
import com.wislie.wanandroid.viewmodel.CoinViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 *    author : Wislie
 *    e-mail : 254457234@qq.comn
 *    date   : 2023/1/14 9:03 AM
 *    desc   : 我的积分列表
 *    version: 1.0
 */
class MyCoinListFragment : BaseViewModelFragment<BaseViewModel, FragmentToolbarListBinding>() {

    private val adapter by lazy {
        MyCoinAdapter()
    }

    private val coinViewModel: CoinViewModel by viewModels()

    override fun init(root: View) {
        super.init(root)

        binding.tb.toolbar.run {
            setNavigationIcon(R.mipmap.ic_back)
            setBackgroundColor(ContextCompat.getColor(hostActivity, R.color.purple_500))
            title = "积分记录"
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
        binding.list.fab.initFab(binding.list.swipeRv)
    }

    override fun loadData() {
        lifecycleScope.launch {
            coinViewModel.myCoinList
                .collectLatest {
                    if (binding.list.swipeRefreshLayout.isRefreshing) {
                        binding.list.swipeRefreshLayout.isRefreshing = false
                    }
                    adapter.submitData(lifecycle, it)
                }
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_toolbar_list
    }
}