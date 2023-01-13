package com.wislie.common.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer


/**
 * 发送与监听
 */
class LiveDataBusManager private constructor() {


    fun <T> post(key: String, tClass: Class<T>, value: T) {
        LiveDataBusKT.with(key, tClass, isSticky = false)
            .postValue(value)
    }

    fun <T> observe(owner: LifecycleOwner, key: String, tClass: Class<T>) {

        //切记：不能写成SAM
        val instance = object : Observer<T> {
            override fun onChanged(data: T) {
                when (key) {
                    LOADING -> { //加载中

                    }
                }
            }
        }
        LiveDataBusKT.with(key, tClass, isSticky = false)
            .observe(owner, instance)
    }



    companion object {
        val instance: LiveDataBusManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            LiveDataBusManager()
        }


        //加载中
        const val LOADING = "loading"
    }
}

