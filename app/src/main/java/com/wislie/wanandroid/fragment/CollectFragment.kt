package com.wislie.wanandroid.fragment

import android.view.View

import com.wislie.common.base.BaseFragment
import com.wislie.common.ext.findNav
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.MultiCategoryPagerAdapter
import com.wislie.wanandroid.databinding.FragmentCollectBinding
import com.wislie.wanandroid.ext.setNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

/**
 * 收藏
 */
class CollectFragment:BaseFragment<FragmentCollectBinding>() {

    override fun init(root: View) {
        binding.ivBack.setOnClickListener {
            findNav().navigateUp()
        }
        val fragments = mutableListOf<BaseFragment<*>>()
        fragments.add(CollectArticleListFragment())
        fragments.add(CollectWebsiteListFragment())
        val tabNameList = arrayListOf<String>()
        tabNameList.add("文章")
        tabNameList.add("网址")

        val pagerAdapter = MultiCategoryPagerAdapter(fragments, this)
        binding.vpProject.adapter = pagerAdapter
        binding.vpProject.offscreenPageLimit = fragments.size
        CommonNavigator(hostActivity).setNavigator(
            binding.indicatorProject,
            binding.vpProject,
            tabNameList
        )
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_collect
    }
}