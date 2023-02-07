package com.wislie.wanandroid.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
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
import com.wislie.wanandroid.App
import com.wislie.wanandroid.R
import com.wislie.wanandroid.data.CollectEvent
import com.wislie.wanandroid.databinding.FragmentWebBinding
import com.wislie.wanandroid.ext.startLogin
import com.wislie.wanandroid.util.ArticleType
import com.wislie.wanandroid.viewmodel.ArticlesViewModel
import com.wislie.wanandroid.widget.WebLayout

/**
 * Web
 */
class WebFragment : BaseViewModelFragment<ArticlesViewModel, FragmentWebBinding>() {

    private val articlesViewModel: ArticlesViewModel by viewModels()
    private lateinit var mAgentWeb: AgentWeb

    private lateinit var toolbar: Toolbar

    private var articleType: Int? = null //文章类型
    private var articleId: Int? = null //文章id
    private var articleName: String? = null //文章名称
    private var articleLink: String? = null //文章链接
    private var articleCollect: Boolean? = null //文章是否收藏


    override fun init(root: View) {
        super.init(root)


        arguments?.run {
            articleType = getInt("type")
            articleId = getInt("id")
            articleName = getString("name")
            articleLink = getString("linkUrl")
            articleCollect = getBoolean("collect")
        }


        toolbar = root.findViewById(R.id.toolbar)
        toolbar.run {
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
                        uncollect()
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
        articleType?.run {
            when (articleType) {
                ArticleType.TYPE_WEBSITE -> { //网址
                    collectWebsite()
                }
                ArticleType.TYPE_BANNER -> { //banner

                }
                else -> { //文章
                    articleId?.run {
                        articlesViewModel.collect(this)
                    }
                }
            }
        }
    }

    //取消收藏
    private fun uncollect() {
        articleType?.run {
            when (articleType) {
                ArticleType.TYPE_WEBSITE -> { //网址
                    unCollectWebsite()
                }
                ArticleType.TYPE_BANNER -> { //banner

                }
                else -> { //文章
                    articleId?.run {
                        articlesViewModel.unCollect(this)
                    }
                }
            }
        }
    }

    //收藏网址
    private fun collectWebsite() {
        if (articleName != null && articleLink != null) {
            articlesViewModel.addCollectWebSite(articleName!!, articleLink!!)
        }
    }

    //取消收藏网址
    private fun unCollectWebsite() {
        articleId?.run {
            articlesViewModel.delCollectWebsite(this)
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
            toolbar.title = t
            toolbar.marquee()
        }
    }

    override fun loadData() {
        super.loadData()
        setCollectStatus(articleCollect)
    }

    override fun observeData() {
        super.observeData()
        articlesViewModel.collectLiveData.observe(viewLifecycleOwner) { resultState ->
            parseState(resultState, { id ->  //收藏成功
                setCollectStatus(collect = true)
                App.instance().appViewModel.collectEventLiveData.value =
                    CollectEvent(collect = true, id)
                Toast.makeText(hostActivity, "收藏成功", Toast.LENGTH_SHORT).show()
            }, {
                startLogin()
            })
        }
        articlesViewModel.uncollectLiveData.observe(viewLifecycleOwner) { resultState ->
            parseState(resultState, { id ->  //取消收藏成功
                setCollectStatus(collect = false)
                App.instance().appViewModel.collectEventLiveData.value =
                    CollectEvent(collect = false, id)
                Toast.makeText(hostActivity, "已取消收藏", Toast.LENGTH_SHORT).show()
            }, {
                startLogin()
            })
        }

        //收藏网址
        articlesViewModel.addCollectWebsiteLiveData
            .observe(viewLifecycleOwner) { resultState ->
                parseState(resultState, { webSiteInfo ->
                    setCollectStatus(collect = true)
                    webSiteInfo?.run {
                        App.instance().appViewModel.collectEventLiveData.value =
                            CollectEvent(collect = true, this.id)
                    }
                    Toast.makeText(hostActivity, "收藏成功", Toast.LENGTH_SHORT).show()
                })
            }

        //取消收藏网址
        articlesViewModel.delCollectWebsiteLiveData
            .observe(viewLifecycleOwner) { resultState ->
                parseState(resultState, { id ->
                    setCollectStatus(collect = false)
                    App.instance().appViewModel.collectEventLiveData.value =
                        CollectEvent(collect = false, id)
                    Toast.makeText(hostActivity, "已取消收藏", Toast.LENGTH_SHORT).show()
                })
            }
    }

    /**
     * 设置收藏状态
     */
    private fun setCollectStatus(collect: Boolean?) {
        collect?.run {
            toolbar.menu.findItem(R.id.collect).isVisible = !this //收藏
            toolbar.menu.findItem(R.id.uncollect).isVisible = this //取消收藏
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
