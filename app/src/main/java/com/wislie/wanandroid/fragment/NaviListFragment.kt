package com.wislie.wanandroid.fragment

import android.text.TextUtils
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import com.wislie.common.base.BaseViewModel
import com.wislie.common.base.BaseViewModelFragment
import com.wislie.common.base.parseState
import com.wislie.common.ext.addFreshListener
import com.wislie.common.ext.init
import com.wislie.common.ext.showErrorCallback
import com.wislie.wanandroid.App
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.NaviListAdapter
import com.wislie.wanandroid.databinding.FragmentNaviListBinding
import com.wislie.wanandroid.viewmodel.ArticlesViewModel
import kotlinx.coroutines.launch

/**
 * 导航列表
 */
class NaviListFragment : BaseViewModelFragment<BaseViewModel, FragmentNaviListBinding>() {

    private val articlesViewModel: ArticlesViewModel by viewModels()

    private val adapter: NaviListAdapter by lazy {
        NaviListAdapter()
    }

    override fun init(root: View) {
        super.init(root)
        registerLoadSir(binding.rvNavi) {
            adapter.refresh() //点击即刷新
        }
        binding.swipeRefreshLayout.init(adapter) {
            adapter.refresh() //点击即刷新
        }
        binding.rvNavi.adapter = adapter
        adapter.addFreshListener(mBaseLoadService)
    }

    override fun loadData() {
        super.loadData()
        articlesViewModel.getNaviList()
    }

    override fun observeData() {
        super.observeData()
        articlesViewModel.naviListLiveData.observe(viewLifecycleOwner) { resultState ->
            parseState(resultState, { treeList ->
                treeList?.run {
                    //list 数据填充
                    lifecycleScope.launch {
                        val pagingData = PagingData.from(treeList)
                        adapter.submitData(pagingData)
                    }
                }
            }, { errorMsg ->
                mBaseLoadService.showErrorCallback()
            })
            if (binding.swipeRefreshLayout.isRefreshing) {
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }

        //这是针对于用户登录后的列表收藏更新
        App.instance()
            .appViewModel
            .userInfoLiveData
            .observe(viewLifecycleOwner) { userInfo ->
                val list = adapter.snapshot().items
                if (userInfo == null) { //用户未登录
                    for (i in list.indices) {
                        for(article in list[i].articles){
                            article.collect = false
                        }
                    }
                    adapter.notifyItemRangeChanged(0, list.size, Any())
                } else { //用户已登录
                    for (i in list.indices) {
                        for(article in list[i].articles){
                            if (article.id in userInfo.collectIds) {
                                article.collect = true
                            }
                        }
                    }
                }
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

                    for(article in list[i].articles){
                        if ((article.id == id || (TextUtils.equals(article.title, collectEvent.title) &&
                                    TextUtils.equals(article.link, collectEvent.link) &&
                                    TextUtils.equals(
                                        article.author,
                                        collectEvent.author
                                    ))) && article.collect != collect
                        ) {
                            article.collect = collect
                            adapter.notifyItemChanged(i, Any())
                        }
                    }


                }
            }
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_navi_list
    }
}