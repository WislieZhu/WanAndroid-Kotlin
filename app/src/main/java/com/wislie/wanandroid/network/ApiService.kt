package com.wislie.wanandroid.network

import com.wislie.common.wrapper.ApiPageResponse
import com.wislie.common.wrapper.ApiResponse
import com.wislie.common.wrapper.PagerApiResponse
import com.wislie.wanandroid.data.*
import okhttp3.Call
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface ApiService {

    @POST("user/register")
    @FormUrlEncoded
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String
    ): ApiResponse<UserInfo?>


    @POST("user/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): ApiResponse<UserInfo?>

    @GET("user/logout/json")
    suspend fun logout(): ApiResponse<Any?> //退出登录


    @GET("banner/json")
    suspend fun getBanner(): ApiResponse<List<Banner>?> //获取首页banner

    @GET("article/list/{page}/json")
    suspend fun getArticleList(@Path("page") pageNo: Long): ApiResponse<ApiPageResponse<ArticleInfo>?>  //首页文章列表


    @GET("article/top/json")
    suspend fun getTopArticleList(): ApiResponse<ArrayList<ArticleInfo>?> //置顶文章


    @GET("hotkey/json")
    suspend fun getHotKey(): ApiResponse<List<HotKey>?> //搜索热词


    @POST("article/query/{page}/json")
    @FormUrlEncoded
    suspend fun queryArticles(
        @Path("page") pageNo: Long,
        @Field("k") hotKey: String
    ): ApiResponse<ApiPageResponse<ArticleInfo>?> //搜索文章结果


    @GET("project/tree/json")
    suspend fun getProject(): ApiResponse<List<ProjectCategory>?> //项目分类


    @GET("article/listproject/{page}/json")
    suspend fun getProjectLatest(
        @Path("page") pageNo: Long,
    ): ApiResponse<ApiPageResponse<ArticleInfo>?> //项目列表的最新数据

    @GET("project/list/{page}/json")
    suspend fun getProjectByCategory(
        @Path("page") pageNo: Long,
        @Query("cid") cid: Int
    ): ApiResponse<ApiPageResponse<ArticleInfo>?> //项目列表数据


    @POST("lg/collect/{id}/json")
    suspend fun collect(@Path("id") id: Int): ApiResponse<Any?> //收藏文章

    @POST("lg/uncollect_originId/{id}/json")
    suspend fun uncollect(@Path("id") id: Int): ApiResponse<Any?> //取消收藏文章


    @GET("lg/coin/userinfo/json")
    suspend fun getCoin(): ApiResponse<Coin?> //获取个人积分，需要登录后访问


    @GET("lg/coin/list/{page}/json")
    suspend fun getMyCoinList(
        @Path("page") pageNo: Long,
        @Query("page_size") page_size:Int
    ): ApiResponse<ApiPageResponse<CoinItem>?>  //获取个人积分获取列表，需要登录后访问


}
