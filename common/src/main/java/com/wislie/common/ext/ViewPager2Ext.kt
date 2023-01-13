package com.wislie.common.ext

import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView

fun BottomNavigationView.bindViewPager2(viewPager2: ViewPager2, actionItemId:Int) {
    setOnItemSelectedListener { item: MenuItem ->
        viewPager2.setCurrentItem(item.order, false);
        true
    }
    itemIconTintList = null
    selectedItemId = actionItemId
}

fun ViewPager2.initMain(context: FragmentActivity, map:MutableMap<Int, Fragment>) {

    adapter = object : FragmentStateAdapter(context) {
        override fun getItemCount() = map.size
        override fun createFragment(position: Int): Fragment {
            return map[position]!!
        }

    }
    isUserInputEnabled = false
    offscreenPageLimit = map.size

}