package com.wislie.wanandroid.fragment

import android.text.TextUtils
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.wislie.common.base.BaseViewModelFragment
import com.wislie.common.base.parseState
import com.wislie.common.ext.addStateListener
import com.wislie.common.ext.init
import com.wislie.wanandroid.App
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.LoadStateFooterAdapter
import com.wislie.wanandroid.adapter.WxArticleAdapter
import com.wislie.wanandroid.databinding.FragmentWxArticleBinding
import com.wislie.wanandroid.ext.startLogin
import com.wislie.wanandroid.viewmodel.ArticlesViewModel
import com.wislie.wanandroid.viewmodel.WxArticleStateViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 微信公众号文章
 */
class WxArticleFragment :
    BaseViewModelFragment<WxArticleStateViewModel, FragmentWxArticleBinding>() {

    private var accountId: Int? = null

    private val articlesViewModel: ArticlesViewModel by viewModels()

    private val adapter: WxArticleAdapter by lazy {
        WxArticleAdapter { articleInfo ->
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
        binding.wxArticleStateVm = mViewModel
        accountId = arguments?.getInt("accountId")
        registerLoadSir(binding.list.swipeRv) {
            adapter.refresh() //点击即刷新
        }
        binding.list.swipeRefreshLayout.init{
            adapter.refresh() //点击即刷新
        }
        binding.list.swipeRv.adapter =
            adapter.withLoadStateFooter(footer = LoadStateFooterAdapter { adapter.retry() })
        adapter.addStateListener(hostActivity, mBaseLoadService)

        binding.btnSearch.setOnClickListener {
            loadData()
        }
    }

    override fun loadData() {
        super.loadData()
        accountId?.run {
            val id = this
            lifecycleScope.launch {
                articlesViewModel.getWxArticleList(id, mViewModel?.inputContent?.get())
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
        articlesViewModel.collectResultLiveData.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, { articleInfo ->  //收藏成功
                val list = adapter.snapshot().items
                for (i in list.indices) {
                    if (list[i].id == articleInfo.id) {
                        list[i].collect = true
                        adapter.notifyItemChanged(i, Any())
                        break
                    }
                }
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
                        break
                    }
                }
            }, {
                startLogin()
            })
        }

        //这是针对于用户登录后的列表收藏更新
        App.instance()
            .appViewModel
            .userInfoLiveData
            .observe(viewLifecycleOwner) { userInfo ->
                val list = adapter.snapshot().items
                if (userInfo == null) { //用户未登录
                    for (i in list.indices) {
                        list[i].collect = false
                    }
                    adapter.notifyItemRangeChanged(0, list.size, Any())
                } else { //用户已登录
                    for (i in list.indices) {
                        if (list[i].id in userInfo.collectIds) {
                            list[i].collect = true
                            adapter.notifyItemChanged(i, Any())
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
        return R.layout.fragment_wx_article
    }
}