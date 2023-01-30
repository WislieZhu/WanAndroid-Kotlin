package com.wislie.wanandroid.fragment

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.paging.cachedIn
import androidx.paging.filter
import com.wislie.common.base.BaseViewModel
import com.wislie.common.base.BaseViewModelFragment
import com.wislie.common.base.parseState
import com.wislie.wanandroid.R
import com.wislie.wanandroid.data.HotKey
import com.wislie.wanandroid.databinding.FragmentSearchArticleBinding
import com.wislie.wanandroid.databinding.ItemSearchBinding
import com.wislie.wanandroid.viewmodel.SearchViewModel
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.include_toolbar.*
import com.wislie.common.ext.findNav
import com.wislie.wanandroid.adapter.SearchHistoryAdapter
import com.wislie.wanandroid.ext.addTextListener
import com.wislie.wanandroid.ext.clearSearchHistory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

/**
 * 搜索
 */
class SearchArticleFragment : BaseViewModelFragment<BaseViewModel, FragmentSearchArticleBinding>() {

    private val searchViewModel by lazy {
        SearchViewModel()
    }

    private val adapter by lazy {
        SearchHistoryAdapter { searchKey -> //其实应该有个对话框，是否删除
            searchViewModel.deleteSearchKeyByName(hostActivity, searchKey)
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_search_article
    }

    override fun init(root: View) {
        super.init(root)
        with(toolbar) {
            setBackgroundColor(ContextCompat.getColor(hostActivity, R.color.purple_500))
            setNavigationIcon(R.mipmap.ic_back)
            setNavigationOnClickListener {
                findNav().navigateUp()
            }
            inflateMenu(R.menu.first_page_menu)

            val inputEt = findViewById<EditText>(R.id.et_input_content)
            val closeIv = findViewById<ImageView>(R.id.iv_close)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.home_search -> {
                        //输入的内容
                        val inputSearchContent = inputEt.text.toString()
                        if (!TextUtils.isEmpty(inputSearchContent)) {
                            //插入搜索的内容
                            searchViewModel.insertSearchKey(hostActivity, inputSearchContent)
                            val direction =
                                SearchArticleFragmentDirections.actionFragmentSearchArticleToFragmentSearchArticleResult(
                                    inputSearchContent
                                )
                            findNav().navigate(direction)
                        }
                    }
                }
                true
            }
            inputEt.addTextListener(etAfterTextChanged = { editable ->
                editable?.run {
                    closeIv.visibility = if (this.isEmpty()) {
                        View.INVISIBLE
                    } else {
                        View.VISIBLE
                    }
                }
            })
            closeIv.setOnClickListener {
                inputEt.setText("")
                closeIv.visibility = View.INVISIBLE
            }
        }
        binding.rvSearchHistory.adapter = adapter
        binding.tvClearHistory.setOnClickListener {  //清空
            clearSearchHistory {
                searchViewModel.deleteSearchHistory(hostActivity)
            }
        }
    }

    override fun loadData() {
        searchViewModel.getHotKey()
        lifecycleScope.launch {
            searchViewModel.queryAllSearchKey(hostActivity)
                .cachedIn(scope = lifecycleScope)
                .combine(searchViewModel.removedSearchKeysFlow) { pagingData, removed ->
                    pagingData.filter {
                        it !in removed
                    }
                }
                .collectLatest {
                    adapter.submitData(lifecycle, it)
                }
        }
    }

    override fun observeData() {
        searchViewModel.hotKeyResultLiveData
            .observe(
                viewLifecycleOwner
            ) { resultState ->
                parseState(resultState, { hotKeys ->
                    hotKeys?.also(::fillTags)
                })
            }
        searchViewModel.searchKeyLiveData
            .observe(viewLifecycleOwner) { resultState ->
                parseState(resultState, { searchKey ->
                    searchViewModel.removeSearchKey(searchKey)
                })
            }
        searchViewModel.searchKeyDelLiveData
            .observe(viewLifecycleOwner) { resultState ->
                parseState(resultState, { status ->
                    if (status) {
                        searchViewModel.removeAllSearchKeys()
                    }
                })
            }
    }

    private fun fillTags(hotKeyList: List<HotKey>) {
        binding.hotKeyFlowlayout.adapter = object : TagAdapter<HotKey>(hotKeyList) {
            override fun getView(parent: FlowLayout?, position: Int, t: HotKey): View {
                val binding = DataBindingUtil.inflate<ItemSearchBinding>(
                    LayoutInflater.from(hostActivity),
                    R.layout.item_search,
                    parent,
                    false
                )
                binding.hotKey = t
                return binding.root
            }
        }
        binding.hotKeyFlowlayout.setOnTagClickListener { _, position, _ ->
            val hotKey = hotKeyList[position].name
            //插入搜索的内容
            searchViewModel.insertSearchKey(hostActivity, hotKey)
            val direction =
                SearchArticleFragmentDirections.actionFragmentSearchArticleToFragmentSearchArticleResult(
                    hotKey
                )
            findNav().navigate(direction)
            true
        }
    }
}