package com.wislie.common.util

import android.content.Context
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager


object KeyboardUtil {

    fun closeSoftKeyboard(view: View?) {
        if (view == null || view.windowToken == null) {
            return
        }
        val imm =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    fun shouldHideInputMethod(focusView: View, event: MotionEvent): Boolean {
        val rect = Rect()
        focusView.getHitRect(rect)
        return !rect.contains(event.x.toInt(), event.y.toInt())
    }
}