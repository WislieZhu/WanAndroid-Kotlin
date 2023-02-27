package com.wislie.wanandroid.ext

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 *    author : Wislie
 *    e-mail : 254457234@qq.comn
 *    date   : 2023/1/22 9:28 PM
 *    desc   :
 *    version: 1.0
 */
fun FloatingActionButton.initFab(recyclerView: RecyclerView){
    val scrollListener = object: RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == SCROLL_STATE_IDLE) {
                // 判断第一条item是否可见，如果不可见则显示回顶部按钮

                recyclerView.layoutManager?.run {
                    if(this.findViewByPosition(0) != null){
                        if (visibility == View.VISIBLE) {
                            visibility = View.INVISIBLE; // 设置滑动顶部按钮不可见
                        }
                    }else {
                        if (visibility == View.INVISIBLE) {
                            visibility = View.VISIBLE; // 设置滑动顶部按钮可见
                        }
                    }
                }
            }
        }
    }

    val clickListener = View.OnClickListener { recyclerView.smoothScrollToPosition(0) }
    recyclerView.addOnScrollListener(scrollListener)
    this.setOnClickListener(clickListener)
}