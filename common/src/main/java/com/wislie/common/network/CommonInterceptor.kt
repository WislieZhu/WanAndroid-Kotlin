package com.wislie.common.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class CommonInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val builder = request.url().newBuilder()

        builder.setQueryParameter(
            "token",
//            "72b32edd68454311b49c13445577bcab" //陈永博
        "5bd571f12e8e454e9e10919f0a9d7cdf" //后人龙
        ) //需要使用setQueryParameter
        val httpUrl = builder
            .setQueryParameter("dtype", "1")
            .build()


//        Log.i("url", "intercept url=" + httpUrl.url())


        val newRequest = request.newBuilder()
            .url(httpUrl)
            .build()


        return chain.proceed(newRequest)

    }
}
