package com.wislie.wanandroid.ext

import android.view.View
import com.wislie.common.base.BaseFragment
import com.wislie.common.ext.findNav
import com.wislie.wanandroid.R
import com.wislie.wanandroid.util.Settings


/**
 * 跳转到登录页
 */
fun BaseFragment<*>.startLogin() {
    findNav().navigate(R.id.fragment_login)
}

fun View.startLogin() {
    findNav().navigate(R.id.fragment_login)
}

/**
 * 跳转目标fragment
 */
fun BaseFragment<*>.startDestination(block: () -> Unit) {
    if (Settings.isLogined) {
        block.invoke()
    } else {
        startLogin()
    }
}