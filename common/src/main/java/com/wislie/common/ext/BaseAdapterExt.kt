package com.wislie.common.ext

import android.graphics.Color
import android.util.Log
import androidx.paging.PagingDataAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kingja.loadsir.core.LoadService
import com.wislie.common.base.BaseAdapter
import com.wislie.common.base.State

/**
 * 监听刷新
 */
fun BaseAdapter<*, *, *>.addFreshListener(
    loadService: LoadService<*>
) {
    setOnRefreshStateListener {
        when (it) {
            is State.Loading -> {
//                Log.i("wislieZhu","adapter loading -- ${adapter.javaClass.simpleName}")
                loadService.showLoadCallback()
            }
            is State.Success -> {
//                Log.i("wislieZhu","adapter success -- ${adapter.javaClass.simpleName}")
                if (itemCount == 0) {
                    loadService.showEmptyCallback()
                } else {
                    loadService.showSuccess()
                }
            }
            is State.Error -> {
//                Log.i("wislieZhu","adapter error -- ${adapter.javaClass.simpleName}")
                loadService.showErrorCallback()
            }
        }
    }
}

/**
 * 刷新
 */
fun SwipeRefreshLayout.init(adapter: PagingDataAdapter<*, *>, doRefresh: (() -> Unit)? = null) {
    this.setColorSchemeColors(Color.rgb(47, 223, 189))
    setOnRefreshListener {
        doRefresh?.invoke()
    }
}