package com.wislie.wanandroid.fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.wislie.common.base.BaseViewModel
import com.wislie.common.base.BaseViewModelFragment
import com.wislie.common.base.parseState
import com.wislie.common.util.Utils
import com.wislie.wanandroid.R
import com.wislie.wanandroid.data.HotKey
import com.wislie.wanandroid.databinding.FragmentSearchArticleBinding
import com.wislie.wanandroid.databinding.ItemSearchBinding
import com.wislie.wanandroid.viewmodel.SearchViewModel
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.include_toolbar.*
import com.wislie.common.ext.findNav

/**
 * 搜索
 */
class SearchArticleFragment : BaseViewModelFragment<BaseViewModel, FragmentSearchArticleBinding>() {

    private val searchViewModel by lazy {
        SearchViewModel()
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_search_article
    }

    override fun init(root: View) {
        super.init(root)
        with(toolbar) {
            setBackgroundColor(ContextCompat.getColor(Utils.getApp(), R.color.purple_500))
            setNavigationIcon(R.mipmap.ic_back)
            setNavigationOnClickListener {
                findNav().navigateUp()
            }
            inflateMenu(R.menu.first_page_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.home_search ->
                        Log.i("xxx", "12345")
                }
                true
            }
        }
    }

    override fun loadData() {
        searchViewModel.getHotKey()
    }

    override fun observeData() {
        searchViewModel.hotKeyResultLiveData
            .observe(
                viewLifecycleOwner
            ) { resultState ->
                parseState(resultState,{ hotKeys ->
                    hotKeys?.also(::fillTags)
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
            val direction =
                SearchArticleFragmentDirections.actionFragmentSearchArticleToFragmentSearchArticleResult(
                    hotKey
                )
            findNav().navigate(direction)
            true
        }
    }
}