package com.wislie.common.test

import android.util.Log


/**
 * 耗时监视器对象，记录整个过程的耗时情况，可以用在很多需要统计的地方，比如Activity的启动耗时和Fragment的启动耗时。
 */
class TimeMonitor(private val mMonitorId: Int = -1) {
    private val TAG = TimeMonitor::class.java.simpleName

    // 保存一个耗时统计模块的各种耗时，tag对应某一个阶段的时间
    val timeTags = HashMap<String, Long?>()
    private var mStartTime: Long = 0



    val monitorId: Int
        get() = mMonitorId

    fun startMonitor() {
        // 每次重新启动都把前面的数据清除，避免统计错误的数据
        if (timeTags.size > 0) {
            timeTags.clear()
        }
        mStartTime = System.currentTimeMillis()
    }

    /**
     * 每打一次点，记录某个tag的耗时
     */
    fun recordingTimeTag(tag: String) {
        // 若保存过相同的tag，先清除
        if (timeTags[tag] != null) {
            timeTags.remove(tag)
        }
        val time = System.currentTimeMillis() - mStartTime
        Log.d(TAG, "$tag: $time")
        timeTags[tag] = time
    }

    fun end(tag: String, writeLog: Boolean) {
        recordingTimeTag(tag)
        end(writeLog)
    }

    fun end(writeLog: Boolean) {
        if (writeLog) {
            //写入到本地文件
        }
    }
}