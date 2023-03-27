package com.wislie.wanandroid.test

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.util.concurrent.atomic.AtomicLong

class FragmentStateViewPager2Adapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val mFragmentList = mutableListOf<Fragment>()

    private val mTitleList = mutableListOf<String>()
    private val mIds = mutableListOf<Long>()
    private val mAtomicLong = AtomicLong(0)

    override fun getItemCount(): Int {
        return mFragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return mFragmentList[position]
    }

    fun addFragment(fragment: Fragment, title: String): FragmentStateViewPager2Adapter {
        if (fragment != null) {
            mFragmentList += fragment
            mTitleList += title
            mIds += getAtomicGeneratedId()
        }
        notifyItemInserted(mFragmentList.size - 1)
        return this
    }

    fun addFragment(index: Int, fragment: Fragment, title: String): FragmentStateViewPager2Adapter {
        if (fragment != null && index >= 0 && index <= mFragmentList.size) {
            mFragmentList.add(index, fragment)
            mTitleList.add(index, title)
            mIds.add(index, getAtomicGeneratedId())
            notifyItemInserted(mFragmentList.size - 1)
        }
        return this
    }

    fun removeFragment(index: Int): FragmentStateViewPager2Adapter {
        if (index >= 0 && index <= mFragmentList.size) {
            mFragmentList.removeAt(index)
            mTitleList.removeAt(index)
            mIds.removeAt(index)
//            notifyItemRemoved(index)
            notifyDataSetChanged()
        }
        return this
    }

    fun removeAllFragment(): FragmentStateViewPager2Adapter {
        mFragmentList.clear()
        mTitleList.clear()
        mIds.clear()
        notifyDataSetChanged()
        return this
    }


    fun getPageTitle(position: Int): String {
        return mTitleList[position]
    }

    override fun getItemId(position: Int): Long {
        if (position >= itemCount || position < 0)
            return RecyclerView.NO_ID
        return mIds[position]
    }

    override fun containsItem(itemId: Long): Boolean {
        return mIds.contains(itemId)
    }

    private fun getAtomicGeneratedId(): Long {
        return mAtomicLong.incrementAndGet()
    }
}