package com.wislie.wanandroid.util

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StudentCoroutine {

    fun getTeacher(){
        GlobalScope.launch(Dispatchers.Main) {
            Log.i("wislieZhu","start")
            val t1 = System.currentTimeMillis()
            var studentId: Int

            var teacherId = 0
            //先获取学生信息
            //我的理解 withContext(Dispatchers.IO){ } 是一个挂起操作, 这里执行完，才轮到后续执行
            withContext(Dispatchers.IO) { //子协程
                //模拟网络获取
                Thread.sleep(2000)
                studentId = 100
            }
            //再获取教师信息
            withContext(Dispatchers.IO) {
                if (studentId === 100) {
                    //模拟网络获取
                    Thread.sleep(2000)
                    teacherId = 200
                }
            }
            val t2 = System.currentTimeMillis() - t1
            //更新UI
            Log.i("wislieZhu","studentId=$studentId teacherId=$teacherId 时间戳=$t2")
        }
    }



}


