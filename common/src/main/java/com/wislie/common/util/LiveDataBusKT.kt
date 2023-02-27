package com.wislie.common.util;

import androidx.arch.core.internal.SafeIterableMap
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 *    author : Wislie
 *    e-mail : 254457234@qq.comn
 *    date   : 11/18/21 10:23 AM
 *    desc   : hook LiveData，原理是将lastVersion的值设置成当前Version的值
 *    version: 1.0
 */
object LiveDataBusKT {

    private val bus: MutableMap<String, BusMutableLiveData<Any>> by lazy {
        HashMap()
    }

    @Synchronized
    fun <T> with(key: String, type: Class<T>, isSticky: Boolean = true): BusMutableLiveData<T> {
        if (!bus.containsKey(key)) {
            bus[key] = BusMutableLiveData(isSticky)
        }
        return bus[key] as BusMutableLiveData<T>
    }

    fun getInstance():MutableMap<String, BusMutableLiveData<Any>> = bus

    class BusMutableLiveData<T>() : MutableLiveData<T>() {

        var isSticky: Boolean = false

        constructor(isSticky: Boolean) : this() {
            this.isSticky = isSticky
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            super.observe(owner, observer)
            if (!isSticky) { //去除黏性
                hook(observer)
            }
        }

        private fun hook(observer: Observer<in T>) {

            //得到当前mVersion的值
            var currentVersionField = LiveData::class.java.getDeclaredField("mVersion")
            currentVersionField.isAccessible = true
            var currentVersion = currentVersionField.get(this)

            //得到lastVersion的field
            var observersField = LiveData::class.java.getDeclaredField("mObservers")
            observersField.isAccessible = true
            //得到SafeIterableMap<Observer<? super T>, ObserverWrapper> mObservers对象
            var observers = observersField.get(this)

            var getMethod = SafeIterableMap::class.java.getDeclaredMethod("get", Object::class.java)
            getMethod.isAccessible = true
            // 返回值 Entry<K, V>
            var entry = getMethod.invoke(observers, observer)

            if (entry != null && entry is Map.Entry<*, *>) {
                var observerWrapper = entry.value!!
                var lastVersionField = observerWrapper.javaClass.superclass.getDeclaredField("mLastVersion")
                //替换lastVersionField的值
                lastVersionField.isAccessible = true
                lastVersionField.set(observerWrapper, currentVersion)
            }
        }

    }
}

