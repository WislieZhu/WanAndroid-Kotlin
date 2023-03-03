package com.wislie.common.network

import android.util.Log
import com.wislie.common.util.NetworkUtil
import com.wislie.common.util.Utils
import okhttp3.Interceptor
import okhttp3.Response
import java.net.HttpURLConnection

class LogInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val response = chain.proceed(request)
        //第一种情况:没有网络，也没有缓存， 返回504
        if (response.cacheResponse() == null && !NetworkUtil.isNetworkAvailable(
                Utils.getApp())) {

        }
        //第二种情况:没有网络，本地有缓存,使用本地缓存
        if (response.cacheResponse() != null && !NetworkUtil.isNetworkAvailable(
               Utils.getApp())) {
            Log.i("WislieZhu", "从缓存中获取数据")

        }
        //第三种情况:访问服务器资源，返回的状态码为304,本地有缓存,使用本地缓存
        if (response.cacheResponse() != null) {
            response.networkResponse()?.let { networkResponse ->
                if (networkResponse.code() == HttpURLConnection.HTTP_NOT_MODIFIED) {
                    Log.i("WislieZhu", "从缓存中获取数据 code=${networkResponse.code()}")
                }
            }
        }
        //第四种情况:访问服务器资源，资源发生变化,使用网络访问
        response.networkResponse()?.let {networkResponse ->
            if (networkResponse.code() != HttpURLConnection.HTTP_NOT_MODIFIED) {
                Log.i("WislieZhu", "从网络中获取数据 code=${networkResponse.code()} ")
            }
        }

        return response
    }
}