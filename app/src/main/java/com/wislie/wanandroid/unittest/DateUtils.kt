package com.wislie.wanandroid.unittest

import java.util.*

object DateUtils {
    fun getValidDate(milliseconds: Long): Long {
        var validDate = milliseconds
        val timeInMillis = Calendar.getInstance(Locale.US).timeInMillis
        if (milliseconds <= 0L || milliseconds > timeInMillis) {
            validDate = timeInMillis
        }
        return validDate
    }
}