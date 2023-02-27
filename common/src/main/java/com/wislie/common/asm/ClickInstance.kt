package com.wislie.common.asm

import android.view.View


object ClickInstance {

    private const val duration: Long = 3000

    private val clickMap = mutableMapOf<View,Long>()

    @JvmStatic
    fun isNotRepeatClick(view:View): Boolean {

        if(clickMap[view] == null){
            clickMap[view] = System.currentTimeMillis()
            return true
        }

        val lastClickMills = clickMap[view]
        val now = System.currentTimeMillis()
        if (lastClickMills == null || now - lastClickMills >= duration) {
            clickMap[view] = now
            return true
        }
        return false
    }
}