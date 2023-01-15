package com.wislie.wanandroid.fragment

import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.wislie.common.base.*
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.FirstPageArticleAdapter
import com.wislie.common.ext.addFreshListener
import com.wislie.common.ext.findNav
import com.wislie.common.ext.init
import com.wislie.common.util.Utils
import com.wislie.wanandroid.App
import com.wislie.wanandroid.adapter.BannerPager
import com.wislie.wanandroid.adapter.LoadStateFooterAdapter
import com.wislie.wanandroid.adapter.holder.BannerViewHolder
import com.wislie.wanandroid.data.Banner
import com.wislie.wanandroid.databinding.FragmentFirstPageBinding
import com.wislie.wanandroid.databinding.ItemFirstPageHeaderBinding
import com.wislie.wanandroid.util.startLogin
import com.wislie.wanandroid.viewmodel.ArticlesViewModel
import com.zhpan.bannerview.BannerViewPager
import com.zhpan.bannerview.constants.IndicatorGravity
import com.zhpan.bannerview.utils.BannerUtils
import com.zhpan.indicator.enums.IndicatorStyle
import kotlinx.android.synthetic.main.include_toolbar.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 首页
 */
class FirstPageFragment : BaseViewModelFragment<BaseViewModel, FragmentFirstPageBinding>() {

    private val articlesViewModel: ArticlesViewModel by viewModels()

    private val adapter by lazy {
        FirstPageArticleAdapter { position, articleInfo ->
            articleInfo?.run {
                if (collect != null && collect) {
                    articlesViewModel.uncollect(articleInfo, position)
                } else {
                    articlesViewModel.collect(articleInfo, position)
                }
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

        registerLoadSir(binding.rvArticles) {
            adapter.refresh() //点击即刷新
        }

        binding.swipeRefreshLayout.init(adapter)
        //todo 感觉没什么效果
        binding.rvArticles.adapter =
            adapter.withLoadStateFooter(footer = LoadStateFooterAdapter { adapter.retry() })
        adapter.addFreshListener(mBaseLoadService)

        val header: ItemFirstPageHeaderBinding = DataBindingUtil.inflate(
            LayoutInflater.from(hostActivity),
            R.layout.item_first_page_header, binding.rvArticles, false
        )
        binding.rvArticles.addHeaderView(header.root)

    }

    override fun observeData() {
        articlesViewModel.bannerResultLiveData.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState,{ banners ->
                val header = binding.rvArticles.getChildAt(0)
                if (header != null && header is BannerViewPager<*, *>) {
                    val bannerVp = header as BannerViewPager<Banner, BannerViewHolder>
                    val bannerPager = BannerPager()
                    bannerVp
                        .setIndicatorGravity(IndicatorGravity.CENTER) /*指示器的位置*/
                        .setAdapter(bannerPager)  //设置适配器    必须
                        .setIndicatorStyle(IndicatorStyle.ROUND_RECT)  /*设置指示器样式*/
                        .setIndicatorSliderGap(BannerUtils.dp2px(2F)) /*指示器的间距*/
                        .create(banners)  /*设置数据*/   /*必须*/
                }
            })
        }

        //收藏
        articlesViewModel.collectResultLiveData.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseListState(resultState, { articleInfo, position ->  //收藏成功
                articleInfo.collect = true
                adapter.notifyItemChanged(position, Any())
            }, {
                startLogin()
            })
        }

        //取消收藏
        articlesViewModel.uncollectResultLiveData.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseListState(resultState, { articleInfo, position ->  //取消收藏成功
                articleInfo.collect = false
                adapter.notifyItemChanged(position, Any())
            }, {
                startLogin()
            })
        }

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
    }

    override fun loadData() {
        lifecycleScope.launch {
            articlesViewModel
                .articleList
                .collectLatest {
                    if (binding.swipeRefreshLayout.isRefreshing) {
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                    adapter.submitData(lifecycle, it)
                    articlesViewModel.getBanner()
                }
        }
    }
}

