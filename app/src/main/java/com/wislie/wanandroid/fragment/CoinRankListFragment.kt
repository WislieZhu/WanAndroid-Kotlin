package com.wislie.wanandroid.fragment

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.wislie.common.base.BaseViewModel
import com.wislie.common.base.BaseViewModelFragment
import com.wislie.common.ext.addStateListener
import com.wislie.common.ext.findNav
import com.wislie.common.ext.init
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.CoinRankAdapter
import com.wislie.wanandroid.adapter.LoadStateFooterAdapter
import com.wislie.wanandroid.databinding.FragmentToolbarListBinding
import com.wislie.wanandroid.ext.initFab
import com.wislie.wanandroid.viewmodel.CoinViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 *    author : Wislie
 *    e-mail : 254457234@qq.comn
 *    date   : 2023/1/14 9:03 AM
 *    desc   : 积分排行版
 *    version: 1.0
 */
class CoinRankListFragment : BaseViewModelFragment<BaseViewModel, FragmentToolbarListBinding>() {

    private val adapter by lazy {
        CoinRankAdapter()
    }

    private val coinViewModel: CoinViewModel by viewModels()

    override fun init(root: View) {
        super.init(root)
        binding.tb.toolbar.run {
            setNavigationIcon(R.mipmap.ic_back)
            setBackgroundColor(ContextCompat.getColor(hostActivity, R.color.purple_500))
            title = "积分排行版"
            setTitleTextColor(ContextCompat.getColor(hostActivity, R.color.white))
            setNavigationOnClickListener {
                findNav().navigateUp()
            }
            inflateMenu(R.menu.coin_rank_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.my_coin -> {
                        val direction =
                            CoinRankListFragmentDirections.actionFragmentCoinRankToFragmentMyCoin()
                        findNav()
                            .navigate(direction)
                    }
                }
                true
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
        binding.list.fab.initFab(binding.list.swipeRv)
    }

    override fun loadData() {
        lifecycleScope.launch {
            coinViewModel.coinRankList
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