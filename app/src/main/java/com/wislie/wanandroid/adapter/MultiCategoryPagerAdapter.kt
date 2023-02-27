package com.wislie.wanandroid.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MultiCategoryPagerAdapter(private val childFrags: List<Fragment>, parentFrag: Fragment) :
    FragmentStateAdapter(parentFrag) {


    override fun getItemCount(): Int {
        return childFrags.size
    }

    override fun createFragment(position: Int): Fragment {
        return childFrags[position]
    }
}