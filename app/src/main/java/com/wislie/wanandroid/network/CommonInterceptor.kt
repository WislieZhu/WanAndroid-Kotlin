package com.wislie.wanandroid.network

import com.wislie.wanandroid.util.Settings
import okhttp3.Interceptor
import okhttp3.Response

class CommonInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val builder = request.url().newBuilder()


        val httpUrl = builder
            .setQueryParameter("deviceId", Settings.deviceId)
            .build()


        val newRequest = request.newBuilder()
            .url(httpUrl)
            .build()


        return chain.proceed(newRequest)

    }
}
