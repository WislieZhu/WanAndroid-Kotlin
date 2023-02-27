package com.wislie.wanandroid.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *    author : Wislie
 *    e-mail : 254457234@qq.comn
 *    date   : 2023/1/23 9:07 PM
 *    desc   :
 *    version: 1.0
 */
@Entity
data class SearchKey(@PrimaryKey var hotKey: String, var mills: Long)
