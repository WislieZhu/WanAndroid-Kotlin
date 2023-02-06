package com.wislie.wanandroid.data

import android.os.Parcelable
//import kotlinx.android.parcel.Parcelize
//import kotlinx.android.parcel.RawValue

//@Parcelize
data class ProjectCategory(
    val articleList:   List<Any>,
    val author: String,
    val children:   List<Any>,
    val courseId: Int,
    val cover: String,
    val desc: String,
    val id: Int,
    val lisense: String,
    val lisenseLink: String,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val type: Int,
    val userControlSetTop: Boolean,
    val visible: Int
)