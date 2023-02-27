package com.wislie.common.test


/**
 * 采用单例管理各个耗时统计的数据。
 */
class TimeMonitorManager {
    private var mTimeMonitorMap: HashMap<Int, TimeMonitor> = HashMap()



    /**
     * 初始化打点模块
     */
    fun resetTimeMonitor(id: Int) {
        if (mTimeMonitorMap[id] != null) {
            mTimeMonitorMap.remove(id)
        }
        getTimeMonitor(id).startMonitor()
    }

    /**
     * 获取打点器
     */
    fun getTimeMonitor(id: Int): TimeMonitor {
        var monitor = mTimeMonitorMap[id]
        if (monitor == null) {
            monitor = TimeMonitor(id)
            mTimeMonitorMap[id] = monitor
        }
        return monitor
    }

    companion object {
        private var mTimeMonitorManager: TimeMonitorManager? = null

        @get:Synchronized
        val instance: TimeMonitorManager?
            get() {
                if (mTimeMonitorManager == null) {
                    mTimeMonitorManager = TimeMonitorManager()
                }
                return mTimeMonitorManager
            }
    }
}
