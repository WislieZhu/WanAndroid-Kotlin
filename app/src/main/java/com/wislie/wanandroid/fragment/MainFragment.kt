package com.wislie.wanandroid.fragment

import android.view.View
import androidx.fragment.app.Fragment
import com.wislie.common.base.BaseFragment
import com.wislie.common.ext.bindViewPager2
import com.wislie.common.ext.initMain
import com.wislie.wanandroid.R
import com.wislie.wanandroid.databinding.FragmentMainBinding

/**
 * 主页面
 */
class MainFragment : BaseFragment<FragmentMainBinding>() {
    override fun getLayoutResId() = R.layout.fragment_main

    override fun init(root: View) {

        val map = mutableMapOf<Int, Fragment>(
            0 to FirstPageFragment(),
            1 to ProjectFragment(),
            2 to SystemFragment(),
            3 to WxAccountFragment(),
            4 to MineFragment(),
        )
        binding.mainViewpager.initMain(hostActivity, map)
        binding.bottomNav.bindViewPager2(binding.mainViewpager, R.id.action_first_page)

    }
}