package com.wislie.wanandroid.fragment

import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.wislie.common.base.BaseViewModel
import com.wislie.common.base.BaseViewModelFragment
import com.wislie.common.base.parseState
import com.wislie.common.ext.addStateListener
import com.wislie.common.ext.findNav
import com.wislie.common.ext.init
import com.wislie.wanandroid.App
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.FirstPageArticleAdapter
import com.wislie.wanandroid.adapter.LoadStateFooterAdapter
import com.wislie.wanandroid.data.CollectEvent
import com.wislie.wanandroid.databinding.FragmentToolbarListBinding
import com.wislie.wanandroid.ext.initFab
import com.wislie.wanandroid.ext.startLogin
import com.wislie.wanandroid.util.ARTICLE_AUTHOR
import com.wislie.wanandroid.util.ARTICLE_USER_ID
import com.wislie.wanandroid.viewmodel.ArticlesViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 分享者的文章列表
 */
class ShareAuthorArticleListFragment :
    BaseViewModelFragment<BaseViewModel, FragmentToolbarListBinding>() {

    private val articlesViewModel: ArticlesViewModel by viewModels()

    private var author: String? = null
    private var userId: Int? = null

    private val adapter by lazy {
        FirstPageArticleAdapter { articleInfo ->
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

        arguments?.run {
            author = getString(ARTICLE_AUTHOR)
            userId = getInt(ARTICLE_USER_ID)
        }

        binding.tb.toolbar.run {
            setBackgroundColor(ContextCompat.getColor(hostActivity, R.color.purple_500))
            setNavigationIcon(R.mipmap.ic_back)
            title = author ?: ""
            setTitleTextColor(ContextCompat.getColor(hostActivity, R.color.white))
            setNavigationOnClickListener {
                findNav().navigateUp()
            }
        }


        registerLoadSir(binding.list.swipeRv) {
            adapter.refresh() //点击即刷新
        }
        binding.list.swipeRefreshLayout.init{
            adapter.refresh() //点击即刷新
        }

        binding.list.swipeRv.adapter =
            adapter.withLoadStateFooter(
                footer = LoadStateFooterAdapter(
                    retry = { adapter.retry() })
            )
        adapter.addStateListener(hostActivity, mBaseLoadService)
        binding.list.fab.initFab(hostActivity,binding.list.swipeRv)
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
            },  {
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

    override fun loadData() {
        userId?.run {
            val id = this
            lifecycleScope.launch {
                articlesViewModel
                    .getShareAuthorArticleList(id)
                    .collectLatest {
                        if (binding.list.swipeRefreshLayout.isRefreshing) {
                            binding.list.swipeRefreshLayout.isRefreshing = false
                        }
                        adapter.submitData(lifecycle, it)
                    }
            }
        }

    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_toolbar_list
    }

}