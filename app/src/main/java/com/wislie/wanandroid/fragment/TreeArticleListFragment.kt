package com.wislie.wanandroid.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.cachedIn
import androidx.paging.filter
import com.wislie.common.base.BaseViewModel
import com.wislie.common.base.BaseViewModelFragment
import com.wislie.common.base.parseState
import com.wislie.common.ext.addStateListener
import com.wislie.common.ext.init
import com.wislie.wanandroid.App
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.LoadStateFooterAdapter
import com.wislie.wanandroid.adapter.TreeArticleAdapter
import com.wislie.wanandroid.data.CollectEvent
import com.wislie.wanandroid.databinding.FragmentListBinding
import com.wislie.wanandroid.viewmodel.ArticlesViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch


/**
 * 体系的文章列表
 */
class TreeArticleListFragment :
    BaseViewModelFragment<BaseViewModel, FragmentListBinding>() {

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
        registerLoadSir(binding.list.swipeRv) {
            adapter.refresh() //点击即刷新
        }
        binding.list.swipeRefreshLayout.init{
            adapter.refresh() //点击即刷新
        }
        binding.list.swipeRv.adapter =
            adapter.withLoadStateFooter(footer = LoadStateFooterAdapter { adapter.retry() })
        adapter.addStateListener(hostActivity, mBaseLoadService)
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
                        if (binding.list.swipeRefreshLayout.isRefreshing) {
                            binding.list.swipeRefreshLayout.isRefreshing = false
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
                })
            }

        //这是针对于WebFragment收藏/取消收藏后的列表收藏更新
        App.instance()
            .appViewModel
            .collectEventLiveData
            .observe(viewLifecycleOwner) { collectEvent ->
                val collect = collectEvent.collect
                val id = collectEvent.id
                val list = adapter.snapshot().items
                for (i in list.indices) {    //收藏列表的id 与 首页列表的id 不是同一个
                    if ((list[i].id == id || (TextUtils.equals(list[i].title, collectEvent.title) &&
                                TextUtils.equals(list[i].link, collectEvent.link) &&
                                TextUtils.equals(
                                    list[i].author,
                                    collectEvent.author
                                ))) && list[i].collect != collect
                    ) {
                        list[i].collect = collect
                        adapter.notifyItemChanged(i, Any())
                    }
                }
            }
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_list
    }

    companion object{
        fun newInstance(cid:Int):TreeArticleListFragment{
            return TreeArticleListFragment().apply {
                arguments = Bundle().apply {
                    putInt("cid",cid)
                }
            }
        }
    }
}