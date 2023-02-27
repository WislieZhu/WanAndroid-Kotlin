package com.wislie.common.ext

import android.text.TextUtils
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

/**
 * 设置跑马灯效果
 */
fun Toolbar.marquee(){
    //获取当前对象的变量
    val titleTextViewField = Toolbar::class.java.getDeclaredField("mTitleTextView")
    titleTextViewField.isAccessible = true
    val obj = titleTextViewField.get(this)
    obj?.also { titleView->
        if(titleView is TextView){
            titleView.ellipsize = TextUtils.TruncateAt.MARQUEE
            titleView.marqueeRepeatLimit = -1
            titleView.isSelected = true
            titleView.requestFocus()
        }
    }
}