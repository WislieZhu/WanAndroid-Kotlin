package com.wislie.common.ext

import android.graphics.Color
import androidx.paging.PagingDataAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kingja.loadsir.core.LoadService
import com.wislie.common.base.BaseAdapter
import com.wislie.common.base.State

fun BaseAdapter<*,*,*>.addFreshListener(loadService:  LoadService<*>){

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

fun SwipeRefreshLayout.init(adapter: PagingDataAdapter<*,*>){
    this.setColorSchemeColors(Color.rgb(47, 223, 189))
    setOnRefreshListener {
        adapter.refresh()
    }
}