package com.wislie.wanandroid.fragment

import android.graphics.Bitmap
import android.util.Log
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
import com.wislie.common.base.TAG
import com.wislie.common.ext.findNav
import com.wislie.common.util.Utils
import com.wislie.wanandroid.R
import com.wislie.wanandroid.databinding.FragmentWebBinding
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
        Log.i(TAG, "link=$linkUrl articleId=$articleId")

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

                    }
                    R.id.uncollect -> {

                    }
                    R.id.open_withBrowser -> {

                    }
                }
                true
            }
        }

        collect?.run {
            toolbar.menu.findItem(R.id.collect).isVisible = !this
            toolbar.menu.findItem(R.id.uncollect).isVisible = this
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


    //onKeyDown 待会写

}
