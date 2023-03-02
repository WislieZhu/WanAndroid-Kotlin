package com.wislie.wanandroid.ext

import android.content.Context
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.wislie.common.util.Utils

/**
 *    author : Wislie
 *    e-mail : 254457234@qq.comn
 *    date   : 2023/1/22 9:28 PM
 *    desc   : fab
 *    version: 1.0
 */
fun FloatingActionButton.initFab(context: Context, recyclerView: RecyclerView) {

    val THRESHOLD = 20;
    var distance = 0;
    var visible = true;
    val fab = this
    recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            if (recyclerView.computeVerticalScrollOffset() >= Utils.getScreenHeight(context)) {
                fab.visibility = View.VISIBLE
            } else {
                fab.visibility = View.INVISIBLE
            }

            if (distance > THRESHOLD && visible) {
                //隐藏动画
                visible = false
                val lp = fab.layoutParams as CoordinatorLayout.LayoutParams
                fab.animate().translationY((fab.height + lp.bottomMargin).toFloat()).interpolator =
                    AccelerateInterpolator(3f)
                distance = 0
            } else if (distance < -20 && !visible) {
                //显示动画
                visible = true
                fab.animate().translationY(0f).interpolator = DecelerateInterpolator(3f)
                distance = 0
            }
            if (visible && dy > 0 || (!visible && dy < 0)) {
                distance += dy
            }


        }

    })
    this.setOnClickListener {
        if ((recyclerView.layoutManager as LinearLayoutManager)
                .findLastVisibleItemPosition() >= 30 //这么写不合理, 如果item 占了很大位置
        ) {
            recyclerView.scrollToPosition(0)
        } else {
            recyclerView.smoothScrollToPosition(0)
        }
    }
}