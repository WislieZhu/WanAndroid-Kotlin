package com.wislie.wanandroid.ext

import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 *    author : Wislie
 *    e-mail : 254457234@qq.comn
 *    date   : 2023/1/22 9:28 PM
 *    desc   :
 *    version: 1.0
 */
fun FloatingActionButton.initFab(recyclerView: RecyclerView){

    val threshold = 10//滑动的距离
    var distance = 0

    val fab = this

    recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            Log.i("wislieZhu","00000 visible=$visibility")  //0是可见 4是不可见
            if (distance > threshold && visibility == View.VISIBLE) {
                //隐藏
                visibility = View.INVISIBLE
//                fab.animate().translationY(fab.height + fab.layoutParams.bottomMargin).interpolator =
//                    AccelerateInterpolator(3f)
                distance = 0
                Log.i("wislieZhu","11111")
            } else if (distance < -20 && visibility != View.VISIBLE) {
                //显示
                visibility = View.VISIBLE
                fab.animate().translationY(0f).interpolator = DecelerateInterpolator(3f)
                distance = 0
                Log.i("wislieZhu","22222")
            }
            if (visibility == View.VISIBLE && dy > 0 || (visibility != View.VISIBLE && dy < 0)) {
                distance += dy
                Log.i("wislieZhu","33333")
            }

        }
    })

    this.setOnClickListener {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        //如果当前recyclerview 最后一个视图位置的索引大于等于30，则迅速返回顶部，否则带有滚动动画效果返回到顶部
        if (layoutManager.findLastVisibleItemPosition() >= 30) {
            recyclerView.scrollToPosition(0)//没有动画迅速返回到顶部(马上)
        } else {
            recyclerView.smoothScrollToPosition(0)//有滚动动画返回到顶部(有点慢)
        }
    }

    /*  val scrollListener = object: RecyclerView.OnScrollListener() {

          override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
              super.onScrolled(recyclerView, dx, dy)
              if (!canScrollVertically(-1)) {
                  Log.i("wislieZhu","onScrolled INVISIBLE")
                 visibility = View.INVISIBLE
              }else{
                  Log.i("wislieZhu","onScrolled VISIBLE")
                  visibility = View.VISIBLE
              }
          }
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
      recyclerView.addOnScrollListener(scrollListener)
      */
}