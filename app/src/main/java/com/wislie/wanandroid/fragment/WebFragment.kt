package com.wislie.wanandroid.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.just.agentweb.WebChromeClient
import com.just.agentweb.WebViewClient
import com.wislie.common.base.BaseViewModelFragment
import com.wislie.common.base.parseState
import com.wislie.common.ext.findNav
import com.wislie.common.ext.marquee
import com.wislie.common.ext.showToast
import com.wislie.wanandroid.App
import com.wislie.wanandroid.R
import com.wislie.wanandroid.data.CollectEvent
import com.wislie.wanandroid.databinding.FragmentWebBinding
import com.wislie.wanandroid.ext.startLogin
import com.wislie.wanandroid.util.*
import com.wislie.wanandroid.viewmodel.ArticlesViewModel
import com.wislie.wanandroid.widget.WebLayout

/**
 * Web
 */
class WebFragment : BaseViewModelFragment<ArticlesViewModel, FragmentWebBinding>() {

    private val articlesViewModel: ArticlesViewModel by viewModels()
    private lateinit var mAgentWeb: AgentWeb

    private var articleType: Int? = null //文章类型
    private var articleId: Int? = null //文章id
    private var articleOriginId: Int = -1 // 收藏页
    private var articleTitle: String? = null //文章名称
    private var articleAuthor: String? = null //文章作者
    private var articleLink: String? = null //文章链接
    private var articleCollect: Boolean? = null //文章是否收藏


    override fun init(root: View) {
        super.init(root)
        arguments?.run {
            articleType = getInt(ARTICLE_TYPE)
            articleId = getInt(ARTICLE_ID)
            articleOriginId = getInt(ARTICLE_ORIGIN_ID)
            articleTitle = getString(ARTICLE_TITLE)
            articleAuthor = getString(ARTICLE_AUTHOR)
            articleLink = getString(ARTICLE_LINK)
            articleCollect = getBoolean(ARTICLE_COLLECT)
        }

        binding.tb.toolbar.run {
            setNavigationIcon(R.mipmap.ic_back)
            setBackgroundColor(ContextCompat.getColor(hostActivity, R.color.purple_500))
            setNavigationOnClickListener {
                findNav().navigateUp()
            }
            inflateMenu(R.menu.web_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.share -> {

                    }
                    R.id.collect -> {
                        collect()
                    }
                    R.id.uncollect -> {
                        unCollect()
                    }
                    R.id.open_withBrowser -> { //用浏览器打开 todo 使用手机浏览器
                        val uri = Uri.parse(articleLink)
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    }
                }
                true
            }
        }
        mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(binding.llWebContent, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .setWebChromeClient(mWebChromeClient)
            .setWebViewClient(mWebViewClient)
            .setMainFrameErrorView(com.wislie.common.R.layout.layout_error, -1)
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
            .setWebLayout(WebLayout(hostActivity))
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK) //打开其他应用时，弹窗咨询用户是否前往其他应用
            .interceptUnkownUrl() //拦截找不到相关页面的Scheme
            .createAgentWeb()
            .ready()
            .go(articleLink)
    }

    //收藏
    private fun collect() {
        if (!Settings.logined) {
            startLogin()
            return
        }
        articleType?.run {
            when (this) {
                ArticleType.TYPE_WEBSITE -> { //网址
                    when {
                        articleTitle.isNullOrEmpty() -> return
                        articleLink.isNullOrEmpty() -> return
                        else -> {
                            articlesViewModel.addCollectWebSite(articleTitle!!, articleLink!!)
                        }
                    }
                }
                ArticleType.TYPE_COLLECT_ARTICLE -> { //收藏页
                    when {
                        articleTitle.isNullOrEmpty() -> return
                        articleLink.isNullOrEmpty() -> return
                        else -> {
                            articlesViewModel.collectPage(
                                articleTitle!!,
                                articleAuthor,
                                articleLink!!
                            )
                        }
                    }
                }
                ArticleType.TYPE_LIST_ARTICLE -> { //列表
                    articleId?.run {
                        articlesViewModel.collect(this)
                    }
                }
                else -> {}
            }
        }
    }

    //取消收藏
    private fun unCollect() {
        if (!Settings.logined) {
            startLogin()
            return
        }
        articleType?.run {
            when (articleType) {
                ArticleType.TYPE_WEBSITE -> { //网址
                    articleId?.run {
                        articlesViewModel.delCollectWebsite(this)
                    }
                }
                ArticleType.TYPE_COLLECT_ARTICLE -> { //收藏页
                    articleId?.run {
                        articlesViewModel.unCollectPage(this, articleOriginId)
                    }
                }
                ArticleType.TYPE_LIST_ARTICLE -> { //列表
                    articleId?.run {
                        articlesViewModel.unCollect(this)
                    }
                }
                else -> {}
            }
        }
    }

    private val mWebViewClient: WebViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            //do you  work
            super.onPageStarted(view, url, favicon)
        }
    }
    private val mWebChromeClient: WebChromeClient = object : WebChromeClient() {
        override fun onReceivedTitle(view: WebView, t: String) {
            super.onReceivedTitle(view, t)
            binding.tb.toolbar.title = t
            binding.tb.toolbar.marquee()
        }
    }

    override fun loadData() {
        super.loadData()
        setCollectStatus(articleCollect)
    }

    override fun observeData() {
        super.observeData()
        //列表->收藏
        articlesViewModel.collectLiveData.observe(viewLifecycleOwner) { resultState ->
            parseState(resultState, { id ->  //收藏成功
                setCollectStatus(collect = true)
                App.instance().appViewModel.collectEventLiveData.value =
                    CollectEvent(collect = true, id)
                hostActivity.showToast("收藏成功")
            },  {
                startLogin()
            })
        }

        //收藏页->收藏
        articlesViewModel.collectPageLiveData.observe(viewLifecycleOwner) { resultState ->
            parseState(resultState, { articleInfo ->
                setCollectStatus(collect = true)
                articleInfo?.run {
                    articleId = this.id
                    articleOriginId = this.originId ?: -1
                    App.instance().appViewModel.collectEventLiveData.value =
                        CollectEvent(collect = true, this.id)
                }
                hostActivity.showToast("收藏成功")
            }, {
                startLogin()
            })
        }

        //列表/收藏页 取消收藏
        articlesViewModel.unCollectLiveData.observe(viewLifecycleOwner) { resultState ->
            parseState(resultState, { id ->  //取消收藏成功
                setCollectStatus(collect = false)
                App.instance().appViewModel.collectEventLiveData.value =
                    CollectEvent(
                        collect = false, id, author = articleAuthor,
                        link = articleLink, title = articleTitle
                    )
                hostActivity.showToast("已取消收藏")
            }, {
                startLogin()
            })
        }

        //网址->收藏
        articlesViewModel.addCollectWebsiteLiveData
            .observe(viewLifecycleOwner) { resultState ->
                parseState(resultState, { webSiteInfo ->
                    setCollectStatus(collect = true)
                    webSiteInfo?.run {
                        App.instance().appViewModel.collectEventLiveData.value =
                            CollectEvent(collect = true, this.id)
                    }
                    hostActivity.showToast("收藏成功")
                })
            }

        //网址->取消收藏
        articlesViewModel.delCollectWebsiteLiveData
            .observe(viewLifecycleOwner) { resultState ->
                parseState(resultState, { id ->
                    setCollectStatus(collect = false)
                    App.instance().appViewModel.collectEventLiveData.value =
                        CollectEvent(collect = false, id)
                    hostActivity.showToast("已取消收藏")
                })
            }
    }

    /**
     * 设置收藏状态
     */
    private fun setCollectStatus(collect: Boolean?) {
        collect?.run {
            val isCollect = this
            binding.tb.toolbar.menu.run {
                findItem(R.id.collect).isVisible = !isCollect //收藏
                findItem(R.id.uncollect).isVisible = isCollect //取消收藏
            }
        }
    }

    override fun onResume() {
        mAgentWeb.webLifeCycle.onResume()
        super.onResume()
    }

    override fun onPause() {
        mAgentWeb.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mAgentWeb.webLifeCycle.onDestroy()
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_web
    }
}
