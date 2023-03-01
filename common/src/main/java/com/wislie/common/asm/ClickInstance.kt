package com.wislie.common.asm

import android.util.Log
import android.view.View
import java.util.*


object ClickInstance {


    private val clickMap by lazy {
        WeakHashMap<View,Long>()
    }

    @JvmStatic
    fun isNotRepeatClick(view:View): Boolean {
        if(clickMap[view] == null){
            clickMap[view] = System.currentTimeMillis()
            Log.i("wislieZhu","点击了")
            return true
        }

        val lastClickMills = clickMap[view]
        val now = System.currentTimeMillis()
        if (lastClickMills == null || now - lastClickMills >= 1500) {
            clickMap[view] = now
            Log.i("wislieZhu","点击了")
            return true
        }
        Log.i("wislieZhu","重复点击")
        return false
    }
}