package com.wislie.wanandroid.data

/**
 * 体系信息
 */
data class TreeInfo(
    val articleList: List<ArticleInfo?>,
    val author: String,
    val children: List<TreeInfo?>,
    val courseId: Int,
    val cover: String,
    val desc: String,
    val id: Int,
    val lisense: String,
    val lisenseLink: String,
    val name: String, //title的名称
    val order: Int,
    val parentChapterId: Int,
    val type: Int,
    val userControlSetTop: Boolean,
    val visible: Int
)