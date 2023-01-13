package com.wislie.wanandroid

import android.app.Application

import com.kingja.loadsir.core.LoadSir
import com.tencent.mmkv.MMKV
import com.wislie.common.callback.EmptyCallback
import com.wislie.common.callback.ErrorCallback
import com.wislie.common.callback.LoadingCallback
import com.wislie.common.util.Utils
import com.wislie.wanandroid.viewmodel.AppViewModel
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


class App : Application() {

    val appViewModel : AppViewModel by lazy {
        AppViewModel(this)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Utils.init(this)

        MMKV.initialize(this)

        LoadSir.beginBuilder()
            .addCallback(ErrorCallback()) //错误页面
            .addCallback(LoadingCallback()) //加载页面
            .addCallback(EmptyCallback()) //空页面
            .setDefaultCallback(LoadingCallback::class.java) //设置默认加载状态页
            .commit()
    }

    //单例化的第三种方式：自定义一个非空且只能一次性赋值的委托属性
    companion object {
        private var instance: App by NotNullSingleValueVar()
        fun instance() = instance
    }
    //定义一个属性管理类，进行非空和重复赋值的判断
    private class NotNullSingleValueVar<T> : ReadWriteProperty<Any?, T> {
        private var value: T? = null
        override fun getValue(thisRef: Any?, property: KProperty<*>): T {
            return value ?: throw IllegalStateException("application not initialized")
        }
        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            this.value = if (this.value == null) value
            else throw IllegalStateException("application already initialized")
        }
    }

}