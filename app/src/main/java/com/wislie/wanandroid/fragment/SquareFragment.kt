package com.wislie.wanandroid.fragment

import android.view.View
import com.wislie.common.base.BaseFragment
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.MultiCategoryPagerAdapter
import com.wislie.wanandroid.databinding.FragmentSquareBinding
import com.wislie.wanandroid.ext.setNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

/**
 * 广场
 */
class SquareFragment : BaseFragment<FragmentSquareBinding>() {

    override fun init(root: View) {
        val fragments = mutableListOf<BaseFragment<*>>()
        fragments.add(SquareArticleListFragment())
        fragments.add(TreeListFragment())
        val tabNameList = arrayListOf<String>()
        tabNameList.add("广场")
        tabNameList.add("体系")
        val pagerAdapter = MultiCategoryPagerAdapter(fragments, this)
        binding.vpSquare.adapter = pagerAdapter
        binding.vpSquare.offscreenPageLimit = fragments.size
        CommonNavigator(hostActivity).setNavigator(
            binding.indicatorSquare,
            binding.vpSquare,
            tabNameList
        )
    }


    override fun getLayoutResId(): Int {
        return R.layout.fragment_square
    }


}