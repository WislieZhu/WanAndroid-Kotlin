package com.wislie.wanandroid.util

import android.view.View
import androidx.fragment.app.Fragment
import com.wislie.common.base.BaseFragment
import com.wislie.common.ext.findNav
import com.wislie.wanandroid.R


/**
 * 跳转到登录页
 */
fun BaseFragment<*>.startLogin() {
    findNav().navigate(R.id.fragment_login)
}

fun View.startLogin() {
    findNav().navigate(R.id.fragment_login)
}