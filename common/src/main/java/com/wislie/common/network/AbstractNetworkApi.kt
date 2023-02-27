package com.wislie.common.network

import com.wislie.common.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit

abstract class AbstractNetworkApi {

    private val retrofitHashMap: MutableMap<String, Retrofit> by lazy {
        mutableMapOf()
    }

    abstract fun setOkHttpClientBuilder(builder: OkHttpClient.Builder)

    abstract fun setRetrofitBuilder(builder: Retrofit.Builder)

    fun <T> getApi(serviceClass: Class<T>, baseUrl: String = BuildConfig.baseUrl): T {
        val key = baseUrl + serviceClass.name
        val retrofit: Retrofit = retrofitHashMap[key] ?: with(Retrofit.Builder()) {
            baseUrl(baseUrl)
            client(okHttpClient)
            setRetrofitBuilder(this)
            build().apply {
                retrofitHashMap[key] = this
            }
        }
        return retrofit.create(serviceClass)
    }

    private val okHttpClient: OkHttpClient
        get() {
            return OkHttpClient.Builder().apply {
                setOkHttpClientBuilder(this)
            }.build()
        }


}