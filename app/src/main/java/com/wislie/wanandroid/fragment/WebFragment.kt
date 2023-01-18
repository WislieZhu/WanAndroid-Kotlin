package com.wislie.wanandroid.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.widget.LinearLayout
import android.widget.Toast
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
import com.wislie.wanandroid.util.startLogin
import com.wislie.wanandroid.viewmodel.ArticlesViewModel
import com.wislie.wanandroid.widget.WebLayout
import kotlinx.android.synthetic.main.include_toolbar.*

/**
 * Web
 */
class WebFragment : BaseViewModelFragment<ArticlesViewModel, FragmentWebBinding>() {

    private val articlesViewModel: ArticlesViewModel by viewModels()
    private lateinit var mAgentWeb: AgentWeb

    override fun init(root: View) {
        super.init(root)

        val linkUrl = arguments?.getString("linkUrl")
        val articleId = arguments?.getInt("id")
        val collect = arguments?.getBoolean("collect", false)


        with(toolbar) {
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
                        articleId?.run {
                            articlesViewModel.collect(this)
                        }

                    }
                    R.id.uncollect -> {
                        articleId?.run {
                            articlesViewModel.uncollect(this)
                        }
                    }
                    R.id.open_withBrowser -> { //用浏览器打开 todo 使用手机浏览器
                        val uri = Uri.parse(linkUrl)
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    }
                }
                true
            }

        }
        setCollectStatus(collect)
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
            .go(linkUrl)

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
            parseState(resultState, { id ->  //收藏成功
                setCollectStatus(collect = false)
                App.instance().appViewModel.collectEventLiveData.value =
                    CollectEvent(collect = false, id)
                Toast.makeText(hostActivity, "已取消收藏", Toast.LENGTH_SHORT).show()
            }, {
                startLogin()
            })
        }
    }

    /**
     * 设置收藏状态
     */
    private fun setCollectStatus(collect: Boolean?) {
        collect?.run {
            toolbar.menu.findItem(R.id.collect).isVisible = !this
            toolbar.menu.findItem(R.id.uncollect).isVisible = this
        }
    }

    override fun loadData() {

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
