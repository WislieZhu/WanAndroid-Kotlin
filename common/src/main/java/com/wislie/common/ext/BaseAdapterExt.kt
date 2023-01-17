package com.wislie.common.ext

import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.core.view.contains
import androidx.paging.PagingDataAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kingja.loadsir.core.LoadService
import com.wislie.common.base.BaseAdapter
import com.wislie.common.base.State
import com.yanzhenjie.recyclerview.SwipeRecyclerView

/**
 * 监听刷新
 */
fun BaseAdapter<*, *, *>.addFreshListener(
    loadService: LoadService<*>
) {
    setOnRefreshStateListener {
        when (it) {
            is State.Loading -> {
                loadService.showLoadCallback()
            }
            is State.Success -> {
                if (itemCount == 0) {
                    loadService.showEmptyCallback()
                } else {
                    loadService.showSuccess()
                }
            }
            is State.Error -> {
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
        adapter.refresh()
        doRefresh?.invoke()
    }
}