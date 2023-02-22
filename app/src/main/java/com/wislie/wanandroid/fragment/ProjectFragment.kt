package com.wislie.wanandroid.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.wislie.common.base.BaseViewModel
import com.wislie.common.base.BaseViewModelFragment
import com.wislie.common.base.parseState
import com.wislie.common.ext.showErrorCallback
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.MultiCategoryPagerAdapter
import com.wislie.wanandroid.data.ProjectCategory
import com.wislie.wanandroid.databinding.FragmentProjectBinding
import com.wislie.wanandroid.ext.setNavigator
import com.wislie.wanandroid.viewmodel.ArticlesViewModel
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

/**
 * 项目
 */
class ProjectFragment : BaseViewModelFragment<BaseViewModel, FragmentProjectBinding>() {

    private val projectViewModel: ArticlesViewModel by viewModels()

    override fun init(root: View) {
        super.init(root)
        registerLoadSir(binding.vpProject) {
            loadData()
        }
    }


    override fun loadData() {
        projectViewModel.getProjectCategory()
    }

    override fun observeData() {
        projectViewModel.projectCategoryLiveData
            .observe(viewLifecycleOwner) { resultState ->
                parseState(resultState, { projectCategories ->
                    mBaseLoadService.showSuccess()
                    projectCategories?.also(::createChildFrags)
                }, {
                    mBaseLoadService.showErrorCallback()
                })
            }
    }

    private fun createChildFrags(projectCategoryList: List<ProjectCategory>) {
        val fragments = mutableListOf<ProjectCategoryFragment>()
        addChildFragment(fragments, 0)
        val tabNameList = ArrayList<String>()
        tabNameList.add("最新项目")
        for (i in projectCategoryList.indices) {
            addChildFragment(fragments, projectCategoryList[i].id)
            tabNameList.add(projectCategoryList[i].name)
        }
        val pagerAdapter = MultiCategoryPagerAdapter(fragments, this)
        binding.vpProject.adapter = pagerAdapter
        binding.vpProject.offscreenPageLimit = fragments.size
        CommonNavigator(hostActivity).setNavigator(
            binding.indicatorProject,
            binding.vpProject,
            tabNameList
        )
    }

    private fun addChildFragment(fragments: MutableList<ProjectCategoryFragment>, cid: Int) {
        val childFrag = ProjectCategoryFragment()
        val bundle = Bundle()
        bundle.putInt("cid", cid)
        childFrag.arguments = bundle
        fragments.add(childFrag)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_project
    }
}