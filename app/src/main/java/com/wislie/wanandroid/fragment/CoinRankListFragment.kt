package com.wislie.wanandroid.fragment

import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.wislie.common.base.BaseViewModel
import com.wislie.common.base.BaseViewModelFragment
import com.wislie.common.ext.addFreshListener
import com.wislie.common.ext.findNav
import com.wislie.common.ext.init
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.CoinRankAdapter
import com.wislie.wanandroid.adapter.LoadStateFooterAdapter
import com.wislie.wanandroid.databinding.FragmentCoinRankListBinding
import com.wislie.wanandroid.ext.initFab
import com.wislie.wanandroid.viewmodel.CoinViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException

/**
 *    author : Wislie
 *    e-mail : 254457234@qq.comn
 *    date   : 2023/1/14 9:03 AM
 *    desc   : 积分排行版
 *    version: 1.0
 */
class CoinRankListFragment : BaseViewModelFragment<BaseViewModel, FragmentCoinRankListBinding>() {

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
        binding.list.swipeRefreshLayout.init(adapter){
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

    override fun observeData() {
        super.observeData()
        adapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.NotLoading -> {
                    Log.i("wislieZhu", "is NotLoading")
                }
                is LoadState.Loading -> {
                    Log.i("wislieZhu", "is Loading")
                }
                is LoadState.Error -> {
                    Log.i("wislieZhu", "is Error:")
                    when ((it.refresh as LoadState.Error).error) {
                        is IOException -> {
                            Log.i("wislieZhu", "IOException")
                        }
                        else -> {
                            Log.i("wislieZhu", "others exception")
                        }
                    }
                }
            }
        }
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
        return R.layout.fragment_coin_rank_list
    }
}