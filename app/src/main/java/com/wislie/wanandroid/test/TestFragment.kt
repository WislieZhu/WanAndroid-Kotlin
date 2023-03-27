package com.wislie.wanandroid.test

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.wislie.common.base.BaseFragment
import com.wislie.wanandroid.R
import com.wislie.wanandroid.databinding.FragmentTestBinding

/**
 * https://blog.csdn.net/CSDNHAY/article/details/129395346
 * 标题
 */
class TestFragment : BaseFragment<FragmentTestBinding>() {
    override fun getLayoutResId(): Int {
        return R.layout.fragment_test
    }

    private val mAdapter by lazy {
        FragmentStateViewPager2Adapter(this)
    }

    override fun init(root: View) {


        binding.tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.run {
                    binding.vpTest.setCurrentItem(position, false)
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        binding.vpTest.adapter = mAdapter
        TabLayoutMediator(binding.tabLayout, binding.vpTest) { tab, position ->
            Log.i("wislieZhu","新增标题=${mAdapter.getPageTitle(position)}")
            tab.text = mAdapter.getPageTitle(position)
        }.attach()
        binding.vpTest.offscreenPageLimit = 3

        binding.btnAdd.setOnClickListener {
            add()
        }

        binding.btnRemoveLast.setOnClickListener {
            delLast()
        }

        binding.btnRemoveAll.setOnClickListener {
            mAdapter.removeAllFragment()
        }

        binding.btnRemoveSec.setOnClickListener {
            mAdapter.removeFragment(1)
        }

        /* val fragments = mutableListOf<BaseFragment<*>>()
         fragments.add(TestFragment2())

         val tabNameList = arrayListOf<String>()
         tabNameList.add("文章")

         val pagerAdapter = MultiCategoryPagerAdapter(fragments, this)
         binding.vpTest.adapter = pagerAdapter
         binding.vpTest.offscreenPageLimit = fragments.size
         CommonNavigator(hostActivity).setNavigator(
             binding.indicatorTest,
             binding.vpTest,
             tabNameList
         )*/


        /*binding.btnTestXc.setOnClickListener {

            StudentCoroutine().getTeacher()
        }

        binding.btnXcNotBlock.setOnClickListener {
            lifecycleScope.launch {
                Log.i("wislieZhu", "${Thread.currentThread().name} start ")
                delay(2000)
                Log.i("wislieZhu", "${Thread.currentThread().name} end")
            }
            it.postDelayed({
                Log.i("wislieZhu", "${Thread.currentThread().name} 证明了协程不阻塞")
            }, 1000)
        }*/
    }


    fun add() {
        val count = mAdapter.itemCount
        val f = TestFragment2()
        val b = Bundle().apply {
            putString("content", "aa a$count")
        }
        f.arguments = b
        mAdapter.addFragment(count, f, "aa a$count")


    }

    private fun delLast() {
        val count = mAdapter.itemCount
        mAdapter.removeFragment(count - 1)
    }


}


