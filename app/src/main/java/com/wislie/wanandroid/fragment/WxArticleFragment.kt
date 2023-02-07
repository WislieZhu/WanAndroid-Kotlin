package com.wislie.wanandroid.fragment

import android.view.View
import androidx.fragment.app.viewModels
import com.wislie.common.base.BaseViewModel
import com.wislie.common.base.BaseViewModelFragment
import com.wislie.common.ext.addFreshListener
import com.wislie.common.ext.init
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.LoadStateFooterAdapter
import com.wislie.wanandroid.adapter.WxArticleAdapter
import com.wislie.wanandroid.databinding.FragmentWxArticleBinding
import com.wislie.wanandroid.viewmodel.ArticlesViewModel

/**
 * 微信公众号文章
 */
class WxArticleFragment: BaseViewModelFragment<BaseViewModel, FragmentWxArticleBinding>()  {


    var accountId: Int? = null

    private val articlesViewModel: ArticlesViewModel by viewModels()

    private val adapter: WxArticleAdapter by lazy {
        WxArticleAdapter { position, articleInfo ->

        }
    }

    override fun init(root: View) {
        super.init(root)
        accountId = arguments?.getInt("accountId")
        registerLoadSir(binding.rvWxArticles) {
            adapter.refresh() //点击即刷新
        }
        binding.swipeRefreshLayout.init(adapter)
        binding.rvWxArticles.adapter =
            adapter.withLoadStateFooter(footer = LoadStateFooterAdapter { adapter.retry() })
        adapter.addFreshListener(mBaseLoadService)

    }

    override fun loadData() {
        super.loadData()
        accountId?.run {
            articlesViewModel.getWxArticleList(this)
        }


    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_wx_article
    }
}