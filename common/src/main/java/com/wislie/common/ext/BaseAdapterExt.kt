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
                Log.i("wislieZhu","Loading..")
                loadService.showLoadCallback()
            }
            is State.Success -> {
                Log.i("wislieZhu","Success..")
                if (itemCount == 0) {
                    loadService.showEmptyCallback()
                } else {
                    loadService.showSuccess()
                }
            }
            is State.Error -> {
                Log.i("wislieZhu","Error..")
                loadService.showErrorCallback()
            }
        }
    }
}


fun BaseAdapter<*, *, *>.addMoreListener(
   swipeRecyclerView:  SwipeRecyclerView
) {
    /*setOnLoadMoreStateListener {
        when (it) {
            is State.Loading -> {
                Log.i("wislieZhu","addMoreListener Loading..")
            }
            is State.Success -> {

                Log.i("wislieZhu","addMoreListener Success.. noMoreData=${it.noMoreData}")
                if (it.noMoreData) {
                    swipeRecyclerView.loadMoreFinish(true, false)
                } else {

                }
            }
            is State.Error -> {
                Log.i("wislieZhu","addMoreListener Error..")

            }
        }
    }*/
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