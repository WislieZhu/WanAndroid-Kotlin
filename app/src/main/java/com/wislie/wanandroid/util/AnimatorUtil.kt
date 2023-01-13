package com.wislie.wanandroid.util

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.CycleInterpolator
import android.widget.ImageView

/**
 *    author : Wislie
 *    e-mail : 254457234@qq.comn
 *    date   : 2023/1/8 7:14 PM
 *    desc   : 动画
 *    version: 1.0
 */
object AnimatorUtil {


    fun startShake(view: ImageView) {
        val animator = ObjectAnimator.ofFloat(view, View.TRANSLATION_X.name, 0f, 8f);//抖动幅度0到8
        animator.duration = 300;//持续时间
        animator.interpolator = CycleInterpolator(4f);//抖动次数
        animator.start();//开始动画

    }
}