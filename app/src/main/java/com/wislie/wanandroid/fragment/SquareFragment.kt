package com.wislie.wanandroid.fragment

import android.view.View
import androidx.viewpager2.widget.ViewPager2.*
import com.wislie.common.base.BaseFragment
import com.wislie.common.ext.findNav
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
        binding.tb.toolbar.apply {
            inflateMenu(R.menu.square_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.square_share -> { //分享

                    }
                    R.id.tree_search -> { //搜索
                        findNav().navigate(R.id.fragment_search_article_by_author)
                    }
                }
                true
            }
            setNavigationOnClickListener {
                findNav().navigateUp()
            }
        }
        val fragments = mutableListOf<BaseFragment<*>>()
        fragments.add(SquareArticleListFragment())
        fragments.add(TreeListFragment())
        fragments.add(NaviListFragment())
        fragments.add(WendaListFragment())
        val tabNameList = arrayListOf<String>()
        tabNameList.add("广场")
        tabNameList.add("体系")
        tabNameList.add("导航")
        tabNameList.add("每日一问")
        val pagerAdapter = MultiCategoryPagerAdapter(fragments, this)
        binding.vpSquare.adapter = pagerAdapter
        binding.vpSquare.offscreenPageLimit = fragments.size
        CommonNavigator(hostActivity).setNavigator(
            binding.indicatorSquare,
            binding.vpSquare,
            tabNameList
        )
        binding.vpSquare.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        binding.tb.toolbar.menu.run {
                            findItem(R.id.square_share).isVisible = true
                            findItem(R.id.tree_search).isVisible = false
                        }
                    }
                    1 -> {
                        binding.tb.toolbar.menu.run {
                            findItem(R.id.square_share).isVisible = false
                            findItem(R.id.tree_search).isVisible = true
                        }
                    }
                    else -> {
                        binding.tb.toolbar.menu.run {
                            findItem(R.id.square_share).isVisible = false
                            findItem(R.id.tree_search).isVisible = false
                        }
                    }
                }
            }
        })
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_square
    }
}