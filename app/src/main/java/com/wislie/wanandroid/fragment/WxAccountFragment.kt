package com.wislie.wanandroid.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.wislie.common.base.BaseViewModel
import com.wislie.common.base.BaseViewModelFragment
import com.wislie.common.base.parseState
import com.wislie.common.ext.showErrorCallback
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.MultiCategoryPagerAdapter
import com.wislie.wanandroid.data.WxAccountInfo
import com.wislie.wanandroid.databinding.FragmentWxAccountBinding
import com.wislie.wanandroid.ext.setNavigator
import com.wislie.wanandroid.util.ACCOUNT_ID
import com.wislie.wanandroid.viewmodel.ArticlesViewModel
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

class WxAccountFragment : BaseViewModelFragment<BaseViewModel, FragmentWxAccountBinding>() {

    private val articlesViewModel: ArticlesViewModel by viewModels()

    override fun init(root: View) {
        super.init(root)
        registerLoadSir(binding.vpWxAccount) {
            loadData()
        }
    }

    override fun loadData() {
        articlesViewModel.getWxAccountList()
    }

    override fun observeData() {
        super.observeData()
        articlesViewModel.wxAccountListLiveData
            .observe(viewLifecycleOwner) { resultState ->
                parseState(resultState, { categories ->
                    mBaseLoadService.showSuccess()
                    categories?.also(::createChildFrags)
                }, {
                    mBaseLoadService.showErrorCallback()
                })
            }
    }

    private fun createChildFrags(categoryList: List<WxAccountInfo>) {
        val fragments = mutableListOf<WxArticleFragment>()
        val tabNameList = ArrayList<String>()
        for (i in categoryList.indices) {
            addChildFragment(fragments, categoryList[i].id)
            tabNameList.add(categoryList[i].name)
        }
        val pagerAdapter = MultiCategoryPagerAdapter(fragments, this)
        binding.vpWxAccount.adapter = pagerAdapter
        binding.vpWxAccount.offscreenPageLimit = fragments.size
        CommonNavigator(hostActivity).setNavigator(
            binding.indicatorWxAccount,
            binding.vpWxAccount,
            tabNameList
        )
    }

    private fun addChildFragment(fragments: MutableList<WxArticleFragment>, id: Int) {
        val childFrag = WxArticleFragment()
        val bundle = Bundle()
        bundle.putInt(ACCOUNT_ID, id)
        childFrag.arguments = bundle
        fragments.add(childFrag)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_wx_account
    }
}