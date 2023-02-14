package com.wislie.wanandroid.fragment

import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.cachedIn
import androidx.paging.filter
import com.wislie.common.base.BaseViewModel
import com.wislie.common.base.BaseViewModelFragment
import com.wislie.common.base.parseState
import com.wislie.common.ext.addFreshListener
import com.wislie.common.ext.init
import com.wislie.wanandroid.App
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.LoadStateFooterAdapter
import com.wislie.wanandroid.adapter.TreeArticleAdapter
import com.wislie.wanandroid.data.CollectEvent
import com.wislie.wanandroid.databinding.FragmentTreeArticleListBinding
import com.wislie.wanandroid.viewmodel.ArticlesViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch


/**
 * 体系的文章列表
 */
class TreeArticleListFragment :
    BaseViewModelFragment<BaseViewModel, FragmentTreeArticleListBinding>() {

    private val articlesViewModel: ArticlesViewModel by viewModels()
    private var articleId: Int? = null

    private val adapter: TreeArticleAdapter by lazy {
        TreeArticleAdapter { articleInfo ->
            articleInfo?.run {
                if (collect) {
                    articlesViewModel.unCollect(id)
                } else {
                    articlesViewModel.collect(articleInfo)
                }
            }
        }
    }

    override fun init(root: View) {
        super.init(root)
        arguments?.run {
            articleId = getInt("cid")
        }
        registerLoadSir(binding.rvArticles) {
            adapter.refresh() //点击即刷新
        }
        binding.swipeRefreshLayout.init(adapter)
        binding.rvArticles.adapter =
            adapter.withLoadStateFooter(footer = LoadStateFooterAdapter { adapter.retry() })
        adapter.addFreshListener(mBaseLoadService)
    }

    override fun loadData() {
        super.loadData()
        articleId?.run {
            val id = this
            lifecycleScope.launch {
                articlesViewModel
                    .getTreeArticleList(id)
                    .cachedIn(scope = lifecycleScope)
                    .combine(articlesViewModel.mRemovedFlow) { pagingData, removedList ->
                        pagingData.filter {
                            it !in removedList
                        }
                    }
                    .collectLatest {
                        if (binding.swipeRefreshLayout.isRefreshing) {
                            binding.swipeRefreshLayout.isRefreshing = false
                        }
                        adapter.submitData(lifecycle, it)
                    }
            }
        }

    }

    override fun observeData() {
        super.observeData()
        //取消收藏
        articlesViewModel.uncollectLiveData
            .observe(viewLifecycleOwner) { resultState ->
                parseState(resultState, { id ->
                    val list = adapter.snapshot().items
                    for (i in list.indices) {
                        if (list[i].id == id) {
                            articlesViewModel.removeFlowItem(list[i])
                            App.instance().appViewModel.collectEventLiveData.value =
                                CollectEvent(
                                    collect = false, id, author = list[i].author,
                                    link = list[i].link, title = list[i].title
                                )
                            break
                        }
                    }
                }, { errorMsg ->
                })
            }

        //全局性质的取消收藏
        App.instance().appViewModel.collectEventLiveData
            .observe(viewLifecycleOwner, Observer { collectEvent ->
                if (!collectEvent.collect) {
                    val list = adapter.snapshot().items
                    for (i in list.indices) {
                        if (list[i].id == collectEvent.id) {
                            articlesViewModel.removeFlowItem(list[i])
                            return@Observer
                        }
                    }
                }
                loadData()
            })
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_tree_article_list
    }
}