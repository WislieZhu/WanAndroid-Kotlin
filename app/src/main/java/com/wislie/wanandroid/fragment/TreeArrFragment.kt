package com.wislie.wanandroid.fragment

import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wislie.common.base.BaseFragment
import com.wislie.common.ext.findNav
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.MultiCategoryPagerAdapter
import com.wislie.wanandroid.data.TreeInfo
import com.wislie.wanandroid.databinding.FragmentTreeArrBinding
import com.wislie.wanandroid.ext.setNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

/**
 * 点击体系后
 */
class TreeArrFragment : BaseFragment<FragmentTreeArrBinding>() {

    private var articleId: Int? = null
    private var title: String? = null
    private var treeInfoStr: String? = null
    private var selectedIndex: Int? = null

    override fun init(root: View) {

        arguments?.run {
            articleId = getInt("cid")
            title = getString("title")
            treeInfoStr = getString("treeInfo")
        }

        binding.ivBack.setOnClickListener {
            findNav().navigateUp()
        }
        binding.tvTitle.text = title ?: ""
        binding.ivSearch.setOnClickListener {  //搜索

        }

        val fragments = mutableListOf<BaseFragment<*>>()
        val tabNameList = arrayListOf<String>()
        treeInfoStr?.run {
            val list =
                Gson().fromJson<List<TreeInfo>>(this, object : TypeToken<List<TreeInfo>>() {}.type)
            if (list.isNotEmpty() && articleId != null) {
                for (index in list.indices) {
                    fragments.add( TreeArticleListFragment.newInstance(list[index].id))
                    tabNameList.add(list[index].name)
                    if (list[index].id == articleId) {
                        selectedIndex = index
                    }
                }
            }
        }
        val pagerAdapter = MultiCategoryPagerAdapter(fragments, this)
        binding.vpTreeCategory.adapter = pagerAdapter
        binding.vpTreeCategory.offscreenPageLimit = fragments.size
        selectedIndex?.run {
            binding.vpTreeCategory.postDelayed({
                binding.vpTreeCategory.currentItem = this
            }, 100)

        }
        CommonNavigator(hostActivity).setNavigator(
            binding.indicatorTreeCategory,
            binding.vpTreeCategory,
            tabNameList
        )
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_tree_arr
    }
}