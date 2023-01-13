package com.wislie.common.ext

import com.kingja.loadsir.core.LoadService
import com.wislie.common.callback.EmptyCallback
import com.wislie.common.callback.ErrorCallback
import com.wislie.common.callback.LoadingCallback


fun LoadService<*>.showLoadCallback() {
    this.showCallback(LoadingCallback::class.java)
}

fun LoadService<*>.showEmptyCallback() {
    this.showCallback(EmptyCallback::class.java)
}

fun LoadService<*>.showErrorCallback() {
    this.showCallback(ErrorCallback::class.java)
}