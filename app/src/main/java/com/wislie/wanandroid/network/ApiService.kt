package com.wislie.wanandroid.network

import com.wislie.common.wrapper.ApiPageResponse
import com.wislie.common.wrapper.ApiResponse
import com.wislie.wanandroid.data.*
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

    @GET("friend/json")
    suspend fun getUsualWebsite(): ApiResponse<List<UsualWebsite>?> //获取常用网站


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


    @GET("lg/collect/list/{page}/json")
    suspend fun getCollectArticles(
        @Path("page") pageNo: Long
    ): ApiResponse<ApiPageResponse<ArticleInfo>?> //收藏的文章列表

    @POST("lg/collect/{id}/json")
    suspend fun collect(
        @Path("id") id: Int
    ): ApiResponse<Any?> //收藏->文章列表

    @POST("lg/collect/add/json")
    @FormUrlEncoded
    suspend fun collect(
        @Field("title") title: String,
        @Field("author") author: String?,
        @Field("link") link: String
    ): ApiResponse<ArticleInfo?> // 收藏->我的收藏页面

    @POST("lg/uncollect_originId/{id}/json")
    suspend fun uncollect(@Path("id") id: Int): ApiResponse<Any?> //取消收藏->文章列表

    @POST("lg/uncollect/{id}/json")
    @FormUrlEncoded
    suspend fun uncollect(
        @Path("id") id: Int,
        @Field("originId") originId: Int
    ): ApiResponse<Any?> //取消收藏->我的收藏页面

    @GET("lg/collect/usertools/json")
    suspend fun getCollectWebsiteList():
            ApiResponse<List<CollectWebsiteInfo>?> //收藏的网址列表

    @POST("lg/collect/addtool/json")
    @FormUrlEncoded
    suspend fun addCollectWebsite(
        @Field("name") name: String,
        @Field("link") link: String
    ): ApiResponse<CollectWebsiteInfo?> //添加网址收藏

    @POST("lg/collect/deletetool/json")
    @FormUrlEncoded
    suspend fun deleteCollectWebsite(@Field("id") id: Int):
            ApiResponse<Any?> //删除网址收藏

    @GET("wenda/list/{page}/json")
    suspend fun getWendaArticles(
        @Path("page") pageNo: Long
    ): ApiResponse<ApiPageResponse<ArticleInfo>?> //问答列表数据


    @GET("wenda/comments/{id}/json")
    suspend fun getWendaComment(
        @Path("id") id: Int
    ): ApiResponse<ApiPageResponse<ReplyComment>?> //问答评论


    @GET("lg/coin/userinfo/json")
    suspend fun getCoin(): ApiResponse<Coin?> //获取个人积分，需要登录后访问


    @GET("coin/rank/{page}/json")
    suspend fun getCoinRank(
        @Path("page") pageNo: Long
    ): ApiResponse<ApiPageResponse<CoinRankInfo>?> //积分排行榜


    @GET("lg/coin/list/{page}/json")
    suspend fun getMyCoinList(
        @Path("page") pageNo: Long
    ): ApiResponse<ApiPageResponse<CoinItem>?>  //获取个人积分获取列表，需要登录后访问


    @GET("wxarticle/chapters/json")
    suspend fun getWxAccountList(): ApiResponse<List<WxAccountInfo>?> //获取微信公众号列表


    @GET("wxarticle/list/{id}/{page}/json")
    suspend fun getWxHistoryArticleList(
        @Path("id") id: Int, @Path("page") pageNo: Long
    ): ApiResponse<ApiPageResponse<ArticleInfo>?> //查看某个公众号历史数据, 输入k则在某个公众号中搜索历史文章


    @GET("wxarticle/list/{id}/{page}/json")
    suspend fun getWxHistoryArticleList(
        @Path("id") id: Int, @Path("page") pageNo: Long,
        @Query("k") k: String
    ): ApiResponse<ApiPageResponse<ArticleInfo>?> //在某个公众号中搜索历史文章


    @GET("user_article/list/{page}/json")
    suspend fun getSquareArticleList(
        @Path("page") pageNo: Long
    ): ApiResponse<ApiPageResponse<ArticleInfo>?> //广场列表数据


    @GET("tree/json")
    suspend fun getTreeList(): ApiResponse<List<TreeInfo>?> //体系数据

    @GET("article/list/{page}/json")
    suspend fun getTreeArticleList(
        @Path("page") pageNo: Long, @Query("cid") cid: Int
    ): ApiResponse<ApiPageResponse<ArticleInfo>?> //知识体系下的文章


}
