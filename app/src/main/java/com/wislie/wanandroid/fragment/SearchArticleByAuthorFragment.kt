package com.wislie.wanandroid.fragment

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.wislie.common.base.BaseViewModel
import com.wislie.common.base.BaseViewModelFragment
import com.wislie.common.base.parseState
import com.wislie.common.ext.addStateListener
import com.wislie.common.ext.findNav
import com.wislie.common.ext.init
import com.wislie.wanandroid.App
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.CommonArticleAdapter
import com.wislie.wanandroid.adapter.LoadStateFooterAdapter
import com.wislie.wanandroid.data.CollectEvent
import com.wislie.wanandroid.databinding.FragmentSearchArticleByAuthorBinding
import com.wislie.wanandroid.ext.addTextListener
import com.wislie.wanandroid.ext.startLogin
import com.wislie.wanandroid.viewmodel.ArticlesViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 按照作者昵称搜索文章
 */
class SearchArticleByAuthorFragment :
    BaseViewModelFragment<BaseViewModel, FragmentSearchArticleByAuthorBinding>() {

    private val articlesViewModel: ArticlesViewModel by viewModels()

    private val adapter by lazy {
        CommonArticleAdapter { articleInfo ->
            articleInfo?.run {
                if (collect) {
                    articlesViewModel.unCollect(id)
                } else {
                    articlesViewModel.collect(id)
                }
            }
        }
    }

    override fun init(root: View) {
        super.init(root)
        binding.tb.toolbar.run {
            setBackgroundColor(ContextCompat.getColor(hostActivity, R.color.purple_500))
            setNavigationIcon(R.mipmap.ic_back)
            setNavigationOnClickListener {
                findNav().navigateUp()
            }
        }
        binding.tb.etInputContent.hint = "请输入昵称"
        binding.tb.etInputContent.addTextListener(etAfterTextChanged = { editable ->
            editable?.run {
                binding.tb.ivClose.visibility = if (this.isEmpty()) {
                    View.INVISIBLE
                } else {
                    View.VISIBLE
                }
                startSearch(this.toString())
            }
        })
        binding.tb.ivClose.setOnClickListener {
            binding.tb.etInputContent.setText("")
            binding.tb.ivClose.visibility = View.INVISIBLE
            startSearch("")
        }
        registerLoadSir(binding.list.swipeRv) {
            adapter.refresh() //点击即刷新
        }
        binding.list.swipeRefreshLayout.init{
            adapter.refresh()
        }
        binding.list.swipeRv.adapter = adapter.withLoadStateFooter(
            footer = LoadStateFooterAdapter(
                retry = { adapter.retry() })
        )
        adapter.addStateListener(hostActivity, mBaseLoadService)
    }

    override fun loadData() {
        super.loadData()
        startSearch(binding.tb.etInputContent.text.toString())
    }

    private fun startSearch(author: String) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                articlesViewModel
                    .getTreeArticleSearchList(author)
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

        //收藏
        articlesViewModel.collectLiveData.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, { articleId ->  //收藏成功
                val list = adapter.snapshot().items
                for (i in list.indices) {
                    if (list[i].id == articleId) {
                        list[i].collect = true
                        adapter.notifyItemChanged(i, Any())
                        App.instance().appViewModel.collectEventLiveData.value =
                            CollectEvent(collect = true, articleId)
                        break
                    }
                }
            }, {
                startLogin()
            })
        }

        //取消收藏
        articlesViewModel.unCollectLiveData.observe(
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
            }, {
                startLogin()
            })
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_search_article_by_author
    }
}