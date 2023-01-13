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
     * 获取当前Activity（栈中最后一个压入的）
     *
     * @return
     */
    fun currentActivity(): Activity? {
        checkWeakReference()
        if (!mActivityStack.isEmpty()) {
            val ref = mActivityStack.lastElement() as WeakReference<Activity>
            return ref.get()
        }
        return null
    }

    /**
     * 检查弱引用是否释放，若释放，则从栈中清理掉该元素
     */
    private fun checkWeakReference() {
        // 使用迭代器进行安全删除
        val it: MutableIterator<WeakReference<Activity>> = mActivityStack.iterator()
        while (it.hasNext()) {
            val activityReference = it.next()
            val temp = activityReference.get()
            if (temp == null) {
                it.remove()
            }
        }
    }

    /**
     * 关闭当前Activity（栈中最后一个压入的）
     */
    fun finishActivity() {
        val activity = currentActivity()
        if (activity != null && !activity.isFinishing) {
            finishActivity(activity)
        }
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

    /**
     * 关闭指定类名的所有Activity
     *
     * @param cls
     */
    fun finishActivity(cls: Class<*>) {
        if (!mActivityStack.isEmpty()) {
            // 使用迭代器进行安全删除
            val it: MutableIterator<WeakReference<Activity>> = mActivityStack.iterator()
            while (it.hasNext()) {
                val activityReference = it.next()
                val activity = activityReference.get()
                // 清理掉已经释放的activity
                if (activity == null) {
                    it.remove()
                    continue
                }
                if (activity.javaClass == cls) {
                    it.remove()
                    activity.finish()
                }
            }
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        if (!mActivityStack.isEmpty()) {
            val it: Iterator<WeakReference<Activity>> = mActivityStack.iterator()
            while (it.hasNext()) {
                val activityReference = it.next()
                val activity = activityReference.get()
                activity?.finish()
            }
            mActivityStack.clear()
        }
    }

    fun clearAllActivityExcept(currentActivity: Activity?) {
        // 使用迭代器进行安全删除
        val it: MutableIterator<WeakReference<Activity>> = mActivityStack.iterator()
        while (it.hasNext()) {
            val activityReference = it.next()
            val activity = activityReference.get()
            // 清理掉已经释放的activity
            if (activity == null) {
                it.remove()
                continue
            }
            if (activity.javaClass != currentActivity?.javaClass) {
                activity.finish()
                it.remove()
            }
        }
    }

    fun containsActivity(cls: Class<*>): Boolean {
        var contains = false
        // 使用迭代器进行安全删除
        val it: MutableIterator<WeakReference<Activity>> = mActivityStack.iterator()
        while (it.hasNext()) {
            val activityReference = it.next()
            val activity = activityReference.get()
            // 清理掉已经释放的activity
            if (activity == null) {
                it.remove()
                continue
            }
            if (activity.javaClass == cls) {
                contains = true
            }
        }
        return contains
    }

    fun getActivitySize(): Int {
        return mActivityStack.size
    }

    companion object {
        val instance: ActivityManagerUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ActivityManagerUtil()
        }
    }
}