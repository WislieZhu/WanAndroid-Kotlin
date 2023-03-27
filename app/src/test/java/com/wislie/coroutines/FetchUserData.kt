package com.wislie.coroutines

import com.wislie.wanandroid.unittest.DateUtils
import com.wislie.wanandroid.util.Settings
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

class FetchUserData {
// kotlinx-coroutines-test

    @Test
    fun test() {

        //伪造context

//        MMKV.initialize()
//        Settings.nickname = "aaa"
//       println(Settings.nickname)


        val currentTimeMillis2 = System.currentTimeMillis()
        val validDate2 = DateUtils.getValidDate(0)

        val valid = validDate2 in -1000 + currentTimeMillis2..1000 + currentTimeMillis2
        println("valid=$valid")


    }



    // ui单元测试
    //接口单元测试 使用协程
    // 需要用到的库 mockk, robolectric, junit


}