package com.wislie.common.ext

import android.graphics.Color
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


/*fun BaseAdapter<*, *, *>.addMoreListener(
    swipeRecyclerView: SwipeRecyclerView
) {
    setOnLoadMoreStateListener {
        if (it is State.Success) {
            if (it.noMoreData) {
                swipeRecyclerView.loadMoreFinish(true, false)
            }
        }
    }
}*/

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