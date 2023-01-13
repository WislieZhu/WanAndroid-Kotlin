package com.wislie.wanandroid.network


import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.wislie.common.BuildConfig
import com.wislie.common.network.*
import com.wislie.common.util.Utils
import okhttp3.Cache
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

//双重校验锁式-单例 封装NetApiService 方便直接快速调用简单的接口
val apiService: ApiService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    CommonNetworkApi.INSTANCE.getApi(ApiService::class.java)
}


class CommonNetworkApi : AbstractNetworkApi() {

    companion object {
        val INSTANCE: CommonNetworkApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            CommonNetworkApi()
        }
    }


    override fun setOkHttpClientBuilder(builder: okhttp3.OkHttpClient.Builder) {

        with(builder) {
            cache(Cache(File(Utils.getApp().cacheDir, "xx_network_cache"), 10 * 1024 * 1024))
            cookieJar(cookieJar)
            addInterceptor(CacheInterceptor())
            addInterceptor(CommonInterceptor())
            addInterceptor(LogInterceptor())
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(5, TimeUnit.SECONDS)
            writeTimeout(5, TimeUnit.SECONDS)
            if (BuildConfig.DEBUG) {
                val loggingInterceptor: HttpLoggingInterceptor =
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                addNetworkInterceptor(loggingInterceptor)
            }
        }

    }

    override fun setRetrofitBuilder(builder: retrofit2.Retrofit.Builder) {
        with(builder){
            addConverterFactory(NullOrEmptyConverterFactory()) //添加返回为null的处理
            addConverterFactory(GsonConverterFactory.create())
        }
    }


    private val cookieJar: PersistentCookieJar by lazy {
        PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(Utils.getApp()))
    }
}