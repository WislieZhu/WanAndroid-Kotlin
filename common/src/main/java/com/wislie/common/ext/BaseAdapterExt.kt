package com.wislie.common.ext

import android.content.Context
import android.graphics.Color
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kingja.loadsir.core.LoadService
import com.wislie.common.base.BaseAdapter
import com.wislie.common.base.State

/**
 * 监听刷新, 加载更多
 */
fun BaseAdapter<*, *, *>.addStateListener(
    context: Context,
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
                Toast.makeText(context, it.exception.errorMsg, Toast.LENGTH_SHORT).show()
            }
        }
    }
    setOnLoadMoreStateListener {
        when (it) {
            is State.Error -> {
                Toast.makeText(context, it.exception.errorMsg, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    setOnPrependStateListener {
        when (it) {
            is State.Error -> {
                Toast.makeText(context, it.exception.errorMsg, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }
}


/**
 * 刷新
 */
fun SwipeRefreshLayout.init(doRefresh: (() -> Unit)? = null) {
    this.setColorSchemeColors(Color.rgb(47, 223, 189))
    setOnRefreshListener {
        doRefresh?.invoke()
    }
}