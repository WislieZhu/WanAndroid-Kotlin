package com.wislie.wanandroid.fragment

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.wislie.common.base.BaseViewModel
import com.wislie.common.base.BaseViewModelFragment
import com.wislie.common.ext.addFreshListener
import com.wislie.common.ext.init
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.CollectArticleAdapter
import com.wislie.wanandroid.adapter.LoadStateFooterAdapter
import com.wislie.wanandroid.databinding.FragmentCollectArticleListBinding
import com.wislie.wanandroid.viewmodel.ArticlesViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 收藏的文章列表
 */
class CollectArticleListFragment :
    BaseViewModelFragment<BaseViewModel, FragmentCollectArticleListBinding>() {

    private val articlesViewModel: ArticlesViewModel by viewModels()

    private val adapter: CollectArticleAdapter by lazy {
        CollectArticleAdapter { position, articleInfo ->
            articleInfo?.run {
                //取消收藏
                articlesViewModel.unCollectPage(articleInfo, position)
            }
        }
    }

    override fun init(root: View) {
        super.init(root)
        registerLoadSir(binding.rvArticle) {
            adapter.refresh() //点击即刷新
        }
        binding.swipeRefreshLayout.init(adapter)
        binding.rvArticle.adapter =
            adapter.withLoadStateFooter(footer = LoadStateFooterAdapter { adapter.retry() })
        adapter.addFreshListener(mBaseLoadService)
    }

    override fun loadData() {
        super.loadData()
        lifecycleScope.launch {
            articlesViewModel
                .collectArticleList
                .collectLatest {
                    if (binding.swipeRefreshLayout.isRefreshing) {
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                    adapter.submitData(lifecycle, it)
                }
        }
    }

    override fun observeData() {
        super.observeData()

    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_collect_article_list
    }
}