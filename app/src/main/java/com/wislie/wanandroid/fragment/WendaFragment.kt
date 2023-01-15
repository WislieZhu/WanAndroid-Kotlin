package com.wislie.wanandroid.fragment

import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.wislie.common.base.*
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.FirstPageArticleAdapter
import com.wislie.common.ext.findNav
import com.wislie.common.util.Utils
import com.wislie.wanandroid.adapter.LoadStateFooterAdapter
import com.wislie.wanandroid.databinding.FragmentFirstPageBinding
import com.wislie.wanandroid.viewmodel.ArticlesViewModel
import kotlinx.android.synthetic.main.include_toolbar.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException

/**
 * 问答
 */
class WendaFragment : BaseViewModelFragment<BaseViewModel, FragmentFirstPageBinding>() {

    private val articlesViewModel: ArticlesViewModel by viewModels()

    private val adapter by lazy {
        FirstPageArticleAdapter { position, articleInfo ->
            articleInfo?.run {
               /* if (collect != null && collect) {
                    articlesViewModel.uncollect(articleInfo, position)
                } else {
                    articlesViewModel.collect(articleInfo, position)
                }*/
            }
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_first_page
    }

    override fun init(root: View) {
        super.init(root)
        with(toolbar) {
            setBackgroundColor(ContextCompat.getColor(Utils.getApp(), R.color.purple_500))
            title = "玩Android"
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

//        registerLoadSir(binding.rvArticles) {
//            adapter.refresh() //点击即刷新
//        }

//        binding.swipeRefreshLayout.init(adapter)
        //todo 感觉没什么效果
        binding.rvArticles.adapter =
            adapter.withLoadStateFooter(footer = LoadStateFooterAdapter {
                adapter.retry() })
//        adapter.addFreshListener(mBaseLoadService)

        //初始状态添加监听
        adapter.addLoadStateListener {
            when (it.refresh) {

                is LoadState.NotLoading -> {
                    Log.d("wislieZhu", "is NotLoading")
                }
                is LoadState.Loading -> {
                    Log.d("wislieZhu", "is Loading")
                }
                is LoadState.Error -> {
                    Log.d("wislieZhu", "is Error:")
                    when ((it.refresh as LoadState.Error).error) {
                        is IOException -> {
                            Log.d("wislieZhu", "IOException")
                        }
                        else -> {
                            Log.d("wislieZhu", "others exception")
                        }
                    }
                }
            }
        }

    }

    override fun observeData() {


    }

    override fun loadData() {
        lifecycleScope.launch {
            articlesViewModel
                .wendaArticleList
                .collectLatest {
                    /*if (binding.swipeRefreshLayout.isRefreshing) {
                        binding.swipeRefreshLayout.isRefreshing = false
                    }*/
                    adapter.submitData( it)
                }
        }
    }
}

