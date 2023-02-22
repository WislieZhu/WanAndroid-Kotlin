package com.wislie.wanandroid.fragment

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.cachedIn
import androidx.paging.filter
import com.wislie.common.base.BaseViewModel
import com.wislie.common.base.BaseViewModelFragment
import com.wislie.common.base.parseState
import com.wislie.common.ext.addStateListener
import com.wislie.common.ext.findNav
import com.wislie.common.ext.init
import com.wislie.common.ext.showEmptyCallback
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.LoadStateFooterAdapter
import com.wislie.wanandroid.adapter.SharePrivateArticleAdapter
import com.wislie.wanandroid.databinding.FragmentToolbarListBinding
import com.wislie.wanandroid.ext.initFab
import com.wislie.wanandroid.viewmodel.ArticlesViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

/**
 * 自己的分享的文章列表
 */
class SharePrivateArticleListFragment :
    BaseViewModelFragment<BaseViewModel, FragmentToolbarListBinding>() {

    private val articlesViewModel: ArticlesViewModel by viewModels()


    private val adapter by lazy {
        SharePrivateArticleAdapter { articleInfo ->
            articleInfo?.run {
               articlesViewModel.delShareArticleLiveData(this.id)
            }
        }
    }

    override fun init(root: View) {
        super.init(root)

        binding.tb.toolbar.run {
            setBackgroundColor(ContextCompat.getColor(hostActivity, R.color.purple_500))
            setNavigationIcon(R.mipmap.ic_back)
            title = "我分享的文章"
            inflateMenu(R.menu.add_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.item_add -> { //添加
//                        findNav().navigate(R.id.fragment_add_todo)
                    }
                }
                true
            }
            setNavigationOnClickListener {
                findNav().navigateUp()
            }
        }


        registerLoadSir(binding.list.swipeRv) {
            adapter.refresh() //点击即刷新
        }
        binding.list.swipeRefreshLayout.init {
            adapter.refresh() //点击即刷新
        }

        binding.list.swipeRv.adapter =
            adapter.withLoadStateFooter(
                footer = LoadStateFooterAdapter(
                    retry = { adapter.retry() })
            )
        adapter.addStateListener(hostActivity, mBaseLoadService)
        binding.list.fab.initFab(binding.list.swipeRv)
    }

    override fun observeData() {
        super.observeData()

        articlesViewModel.delShareArticleLiveData.observe(viewLifecycleOwner){
                resultState ->
            parseState(resultState, { id ->  //删除
                val list = adapter.snapshot().items
                for (i in list.indices) {
                    if (list[i].id == id) {
                        if(list.size == 1){
                            mBaseLoadService.showEmptyCallback()
                        }
                        articlesViewModel.removeFlowItem(list[i])
                        break
                    }
                }
            })
        }
    }

    override fun loadData() {
        lifecycleScope.launch {
            articlesViewModel
                .getSharePrivateArticleList()
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

    override fun getLayoutResId(): Int {
        return R.layout.fragment_toolbar_list
    }

}