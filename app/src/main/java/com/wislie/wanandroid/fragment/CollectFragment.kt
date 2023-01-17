package com.wislie.wanandroid.fragment

import android.view.View
import com.wislie.common.base.BaseFragment
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.ProjectCategoryPagerAdapter
import com.wislie.wanandroid.databinding.FragmentCollectBinding
import com.wislie.wanandroid.util.setNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

/**
 * 收藏
 */
class CollectFragment:BaseFragment<FragmentCollectBinding>() {



    override fun getLayoutResId(): Int {
        return R.layout.fragment_collect
    }

    override fun init(root: View) {
        val fragments = mutableListOf<BaseFragment<*>>()
        fragments.add(CollectWebsiteListFragment())
        fragments.add(CollectWebsiteListFragment())
        val tabNameList = arrayListOf<String>()
        tabNameList.add("文章")
        tabNameList.add("网址")

        val pagerAdapter = ProjectCategoryPagerAdapter(fragments, this)
        binding.vpProject.adapter = pagerAdapter
        binding.vpProject.offscreenPageLimit = fragments.size
        CommonNavigator(hostActivity).setNavigator(
            binding.indicatorProject,
            binding.vpProject,
            tabNameList
        )
    }
}