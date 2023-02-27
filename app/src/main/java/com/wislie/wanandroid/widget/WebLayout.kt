package com.wislie.wanandroid.widget

import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.just.agentweb.IWebLayout
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout
import com.wislie.wanandroid.R

class WebLayout(activity: AppCompatActivity) : IWebLayout<WebView, ViewGroup> {

    private var mTwinklingRefreshLayout: TwinklingRefreshLayout

    init {
        mTwinklingRefreshLayout =
            LayoutInflater.from(activity)
                .inflate(R.layout.fragment_twk_web, null) as TwinklingRefreshLayout
        mTwinklingRefreshLayout.setPureScrollModeOn()
    }

    override fun getLayout(): ViewGroup {
        return mTwinklingRefreshLayout
    }

    override fun getWebView(): WebView? {
        return mTwinklingRefreshLayout.findViewById(R.id.webView)
    }
}