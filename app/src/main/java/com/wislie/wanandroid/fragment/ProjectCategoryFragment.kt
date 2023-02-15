package com.wislie.wanandroid.fragment

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.wislie.common.base.BaseViewModel
import com.wislie.common.base.BaseViewModelFragment
import com.wislie.common.base.parseListState
import com.wislie.common.base.parseState
import com.wislie.common.ext.addFreshListener
import com.wislie.common.ext.init
import com.wislie.wanandroid.App
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.FirstPageArticleAdapter
import com.wislie.wanandroid.databinding.FragmentProjectArticleBinding
import com.wislie.wanandroid.ext.startLogin
import com.wislie.wanandroid.viewmodel.ArticlesViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 项目分类
 */
class ProjectCategoryFragment :
    BaseViewModelFragment<BaseViewModel, FragmentProjectArticleBinding>() {

    private val projectArticlesViewModel: ArticlesViewModel by viewModels()
    private val adapter by lazy {
        FirstPageArticleAdapter { articleInfo ->
            articleInfo?.run {
                if (collect) {
                    projectArticlesViewModel.unCollect(id)
                } else {
                    projectArticlesViewModel.collect(articleInfo)
                }
            }
        }
    }

    var cid: Int? = null

    override fun init(root: View) {
        super.init(root)
        cid = arguments?.getInt("cid")
        registerLoadSir(binding.rvArticles) {
            adapter.refresh() //点击即刷新
        }
        binding.swipeRefreshLayout.init(adapter){
            adapter.refresh() //点击即刷新
        }
        binding.rvArticles.adapter = adapter
        adapter.addFreshListener(mBaseLoadService)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_project_article
    }

    override fun observeData() {
        //收藏
        projectArticlesViewModel.collectResultLiveData.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseListState(resultState, { articleInfo, position ->  //收藏成功
                articleInfo.collect = true
                adapter.notifyItemChanged(position, Any())
            }, {
                startLogin()
            })
        }

        //取消收藏
        projectArticlesViewModel.uncollectLiveData.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, { id ->
                val list = adapter.snapshot().items
                for (i in list.indices) {
                    if (id == list[i].id) {
                        adapter.notifyItemChanged(i, Any())
                        list[i].collect = false
                        break
                    }
                }
            }, { errorMsg ->
            }, {
                startLogin()
            })
        }
        App.instance()
            .appViewModel
            .userInfoLiveData
            .observe(viewLifecycleOwner) { userInfo ->
                val list = adapter.snapshot().items
                if (userInfo == null) { //用户未登录, 显示未收藏
                    for (i in list.indices) {
                        list[i].collect = false
                    }
                    adapter.notifyItemRangeChanged(0, list.size, Any())
                } else { //用户已登录, 显示收藏
                    for (i in list.indices) {
                        if (list[i].id in userInfo.collectIds) {
                            list[i].collect = true
                            adapter.notifyItemChanged(i, Any())
                        }
                    }
                }
            }
    }

    override fun loadData() {
        cid?.also { id ->
            lifecycleScope.launch {
                projectArticlesViewModel.getArticleListByCategory(id)
                    .collectLatest {
                        if (binding.swipeRefreshLayout.isRefreshing) {
                            binding.swipeRefreshLayout.isRefreshing = false
                        }
                        adapter.submitData(lifecycle, it)
                    }
            }
        }
    }
}