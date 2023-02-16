package com.wislie.common.util

import android.app.Activity
import java.lang.ref.WeakReference
import java.util.*

class ActivityManagerUtil private constructor() {

    private var mActivityStack: Stack<WeakReference<Activity>> = Stack<WeakReference<Activity>>()

    /**
     * 添加Activity到栈
     *
     * @param activity
     */
    fun addActivity(activity: Activity) {
        mActivityStack = Stack<WeakReference<Activity>>()
        val ref = WeakReference(activity)
        mActivityStack.add(ref)
    }



    /**
     * 关闭指定的Activity
     *
     * @param activity
     */
    fun finishActivity(activity: Activity?) {
        if (activity != null && !mActivityStack.isEmpty()) {
            // 使用迭代器进行安全删除
            val it: MutableIterator<WeakReference<Activity>> = mActivityStack.iterator()
            while (it.hasNext()) {
                val activityReference = it.next()
                val temp = activityReference.get()
                // 清理掉已经释放的activity
                if (temp == null) {
                    it.remove()
                    continue
                }
                if (temp === activity) {
                    it.remove()
                }
            }
            activity.finish()
        }
    }


    companion object {
        val instance: ActivityManagerUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ActivityManagerUtil()
        }
    }
}