package com.wislie.wanandroid.util

import com.d.lib.slidelayout.SlideLayout

class SlideHelper {

    private var mSlides: MutableList<SlideLayout> = mutableListOf()

    fun onStateChanged(layout: SlideLayout, open: Boolean) {
        if (open) {
            mSlides.add(layout)
        } else {
            mSlides.remove(layout)
        }
    }

    fun closeAll(layout: SlideLayout): Boolean {
        if (mSlides.size <= 0) {
            return false
        }
        var result = false
        var i = 0
        while (i < mSlides.size) {
            val slide = mSlides[i]
            if (slide != null && slide !== layout) {
                slide.close()
                mSlides.remove(slide) // Unnecessary
                result = true
                i--
            }
            i++
        }
        return result
    }

}