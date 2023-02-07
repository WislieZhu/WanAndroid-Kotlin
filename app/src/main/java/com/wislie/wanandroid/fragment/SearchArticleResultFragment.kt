package com.wislie.wanandroid.fragment

import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.wislie.common.base.BaseViewModelFragment
import com.wislie.common.ext.addFreshListener
import com.wislie.common.ext.findNav
import com.wislie.common.ext.init
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.SearchArticleResultAdapter
import com.wislie.wanandroid.databinding.FragmentSearchArticleResultBinding
import com.wislie.wanandroid.viewmodel.ArticlesViewModel
import com.wislie.wanandroid.viewmodel.SearchViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 文章搜索结果
 */
class SearchArticleResultFragment :
    BaseViewModelFragment<SearchViewModel, FragmentSearchArticleResultBinding>() {

    private val args: SearchArticleResultFragmentArgs by navArgs()
    private val searchViewModel: SearchViewModel by viewModels()
    private val articlesViewModel: ArticlesViewModel by viewModels()
    private val adapter by lazy {
        SearchArticleResultAdapter{ position, articleInfo ->
            articleInfo?.also {
                if (it.collect != null && it.collect) {
                    articlesViewModel.unCollect(articleInfo, position)
                } else {
                    articlesViewModel.collect(articleInfo, position)
                }
            }
        }
    }

    override fun init(root: View) {
        super.init(root)

        root.findViewById<Toolbar>(R.id.toolbar).run {
            setNavigationIcon(R.mipmap.ic_back)
            setBackgroundColor(ContextCompat.getColor(hostActivity, R.color.purple_500))
            title = args.hotKey
            setNavigationOnClickListener {
                findNav().navigateUp()
            }
        }
        registerLoadSir(binding.rvArticles) {
            adapter.refresh() //点击即刷新
        }
        binding.swipeRefreshLayout.init(adapter)
        binding.rvArticles.adapter = adapter
        adapter.addFreshListener(mBaseLoadService)
    }


    override fun getLayoutResId(): Int {
        return R.layout.fragment_search_article_result
    }

    override fun loadData() {
        lifecycleScope.launch {
            searchViewModel.getArticleList(args.hotKey)
                .collectLatest {
                    if (binding.swipeRefreshLayout.isRefreshing) {
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                    adapter.submitData(lifecycle, it)
                }
        }
    }


}