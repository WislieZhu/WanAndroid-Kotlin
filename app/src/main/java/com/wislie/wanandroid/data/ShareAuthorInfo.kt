package com.wislie.wanandroid.data

import com.wislie.common.wrapper.ApiPageResponse

/**
 * 分享者信息列表
 */
data class ShareAuthorInfo(val coinInfo:Coin, val shareArticles: ApiPageResponse<ArticleInfo>)
