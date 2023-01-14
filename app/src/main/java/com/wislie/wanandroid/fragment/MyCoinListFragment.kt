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
import com.wislie.common.util.Utils
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.MyCoinAdapter
import com.wislie.wanandroid.databinding.FragmentMyCoinListBinding
import com.wislie.wanandroid.viewmodel.CoinViewModel
import kotlinx.android.synthetic.main.include_toolbar.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 *    author : Wislie
 *    e-mail : 254457234@qq.comn
 *    date   : 2023/1/14 9:03 AM
 *    desc   : 我的积分列表
 *    version: 1.0
 */
class MyCoinListFragment: BaseViewModelFragment<BaseViewModel, FragmentMyCoinListBinding>() {

    private val adapter by lazy {
        MyCoinAdapter()
    }

    private val coinViewModel:CoinViewModel by viewModels()

    override fun init(root: View) {
        super.init(root)

        with(toolbar) {
            setNavigationIcon(R.mipmap.ic_back)
            setBackgroundColor(ContextCompat.getColor(Utils.getApp(), R.color.purple_500))
            title = "积分记录"
            setNavigationOnClickListener {
                findNav().navigateUp()
            }
        }
        registerLoadSir(binding.rvCoin) {
            adapter.refresh() //点击即刷新
        }
        binding.swipeRefreshLayout.init(adapter)
        binding.rvCoin.adapter = adapter
        adapter.addFreshListener(mBaseLoadService)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_my_coin_list
    }

    override fun loadData() {
        lifecycleScope.launch {
            coinViewModel.myCoinListList
                .collectLatest {
                    if (binding.swipeRefreshLayout.isRefreshing) {
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                    adapter.submitData(lifecycle, it)
                }
        }
    }
}