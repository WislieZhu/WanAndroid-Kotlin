package com.wislie.wanandroid.fragment

import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.wislie.common.base.BaseViewModel
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

    private lateinit var inputSearchEt:EditText
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
            setBackgroundColor(ContextCompat.getColor(hostActivity, R.color.purple_500))
            setNavigationIcon(R.mipmap.ic_back)
            setNavigationOnClickListener {
                findNav().navigateUp()
            }
        }

        inputSearchEt = root.findViewById<EditText>(R.id.et_input_content)
        val closeIv = root.findViewById<ImageView>(R.id.iv_close)
        inputSearchEt.hint = "请输入昵称"
        inputSearchEt.addTextListener(etAfterTextChanged = { editable ->
            editable?.run {
                closeIv.visibility = if (this.isEmpty()) {
                    View.INVISIBLE
                } else {
                    View.VISIBLE
                }
                startSearch(this.toString())
            }
        })
        closeIv.setOnClickListener {
            inputSearchEt.setText("")
            closeIv.visibility = View.INVISIBLE
            startSearch("")
        }
        registerLoadSir(binding.rvSearch) {
            adapter.refresh() //点击即刷新
        }
        binding.swipeRefreshLayout.init(adapter){
            adapter.refresh()
        }
        binding.rvSearch.adapter = adapter.withLoadStateFooter(
            footer = LoadStateFooterAdapter(
                retry = { adapter.retry() })
        )
        adapter.addFreshListener(mBaseLoadService)
    }

    override fun loadData() {
        super.loadData()
        startSearch(inputSearchEt.text.toString())
    }

    private fun startSearch(author: String) {
        lifecycleScope.launch {
            articlesViewModel
                .getTreeArticleSearchList(author)
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
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_search_article_by_author
    }
}