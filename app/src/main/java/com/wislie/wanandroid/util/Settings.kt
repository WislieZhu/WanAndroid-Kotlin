package com.wislie.wanandroid.util

import com.wislie.common.util.Pref

object Settings {

    var logined: Boolean by Pref(false) //是否登录

    var nickname: String by Pref("") //用户名

    var avatar: String by Pref("") //头像
}