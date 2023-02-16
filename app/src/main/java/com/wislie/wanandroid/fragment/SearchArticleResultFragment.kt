package com.wislie.wanandroid.fragment

import android.text.TextUtils
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.wislie.common.base.BaseViewModelFragment
import com.wislie.common.base.parseState
import com.wislie.common.ext.addFreshListener
import com.wislie.common.ext.findNav
import com.wislie.common.ext.init
import com.wislie.wanandroid.App
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.LoadStateFooterAdapter
import com.wislie.wanandroid.adapter.SearchArticleResultAdapter
import com.wislie.wanandroid.data.CollectEvent
import com.wislie.wanandroid.databinding.FragmentSearchArticleResultBinding
import com.wislie.wanandroid.ext.startLogin
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
        SearchArticleResultAdapter { articleInfo ->
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
        binding.swipeRefreshLayout.init(adapter) {
            adapter.refresh() //点击即刷新
        }
        binding.rvArticles.adapter = adapter.withLoadStateFooter(
            footer = LoadStateFooterAdapter(
                retry = { adapter.retry() })
        )
        adapter.addFreshListener(mBaseLoadService)
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

    override fun observeData() {
        super.observeData()

        //收藏
        articlesViewModel.collectResultLiveData.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, { articleInfo ->  //收藏成功
                val list = adapter.snapshot().items
                for (i in list.indices) {
                    if (list[i].id == articleInfo.id) {
                        list[i].collect = true
                        adapter.notifyItemChanged(i, Any())
                        App.instance().appViewModel.collectEventLiveData.value =
                            CollectEvent(collect = true, articleInfo.id)
                        break
                    }
                }
            }, { errorMsg ->
            }, {
                startLogin()
            })
        }

        //取消收藏
        articlesViewModel.uncollectLiveData.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, { id ->
                val list = adapter.snapshot().items
                for (i in list.indices) {
                    if (id == list[i].id) {
                        adapter.notifyItemChanged(i, Any())
                        list[i].collect = false
                        App.instance().appViewModel.collectEventLiveData.value =
                            CollectEvent(collect = false, id)
                        break
                    }
                }
            }, { errorMsg ->
            }, {
                startLogin()
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
        return R.layout.fragment_search_article_result
    }

}