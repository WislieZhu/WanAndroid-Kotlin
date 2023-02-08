package com.wislie.wanandroid.fragment

import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.wislie.common.base.*
import com.wislie.common.ext.addFreshListener
import com.wislie.wanandroid.R
import com.wislie.common.ext.findNav
import com.wislie.common.ext.init
import com.wislie.wanandroid.adapter.LoadStateFooterAdapter
import com.wislie.wanandroid.adapter.WendaArticleAdapter
import com.wislie.wanandroid.databinding.FragmentFirstPageBinding
import com.wislie.wanandroid.viewmodel.ArticlesViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 问答
 */
class WendaFragment : BaseViewModelFragment<BaseViewModel, FragmentFirstPageBinding>() {

    private val articlesViewModel: ArticlesViewModel by viewModels()

    private val adapter by lazy {
        WendaArticleAdapter({ id-> //todo 这是假的
            id?.run {
                articlesViewModel.getWendaCommentList(this)
            }
        }, { position, articleInfo ->
            articleInfo?.run {
                 if (collect) {
                     articlesViewModel.unCollect(id)
                 } else {
                     articlesViewModel.collect(articleInfo, position)
                 }
            }
        })
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_first_page
    }

    override fun init(root: View) {
        super.init(root)
        root.findViewById<Toolbar>(R.id.toolbar).run {
            setBackgroundColor(ContextCompat.getColor(hostActivity, R.color.purple_500))
            title = "问答"
            inflateMenu(R.menu.first_page_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.home_search -> {
                        val direction =
                            MainFragmentDirections.actionFragmentMainToFragmentSearchArticle()
                        findNav()
                            .navigate(direction)
                    }
                }
                true
            }
        }

        registerLoadSir(binding.rvArticles) {
            adapter.refresh() //点击即刷新
        }

        binding.swipeRefreshLayout.init(adapter)
        binding.rvArticles.adapter =
            adapter.withLoadStateFooter(footer = LoadStateFooterAdapter(retry = {
                adapter.retry()
            }))
        adapter.addFreshListener(mBaseLoadService)
    }

    override fun observeData() {


    }

    override fun loadData() {
        lifecycleScope.launch {
            articlesViewModel
                .wendaArticleList
                .collectLatest {
                    if (binding.swipeRefreshLayout.isRefreshing) {
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                    adapter.submitData(lifecycle, it)
                }
        }
    }
}

