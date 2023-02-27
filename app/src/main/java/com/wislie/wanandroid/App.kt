package com.wislie.wanandroid

//import com.github.moduth.blockcanary.BlockCanary
//import com.github.moduth.blockcanary.BlockCanaryInternals
import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import com.github.anrwatchdog.ANRWatchDog
import com.kingja.loadsir.core.LoadSir
import com.tencent.mmkv.MMKV
import com.wislie.common.callback.EmptyCallback
import com.wislie.common.callback.ErrorCallback
import com.wislie.common.callback.LoadingCallback
import com.wislie.common.test.TimeMonitorConfig
import com.wislie.common.test.TimeMonitorManager
import com.wislie.common.util.Utils
import com.wislie.wanandroid.anr.ANRFileObserver
import com.wislie.wanandroid.viewmodel.AppViewModel
import java.io.File
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


class App : Application() {

    val appViewModel: AppViewModel by lazy {
        AppViewModel(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        TimeMonitorManager.instance?.resetTimeMonitor(TimeMonitorConfig.TIME_MONITOR_ID_APPLICATION_START)
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

        TimeMonitorManager.instance?.getTimeMonitor(TimeMonitorConfig.TIME_MONITOR_ID_APPLICATION_START)
            ?.recordingTimeTag("Application-onCreate")


        /*if(Utils.isMainProcess(this)){
            Log.i("wislieZhu","这是主进程..")
            //注意在主进程初始化调用
            BlockCanary.install(this, AppBlockCanaryContext()).start()
        }else{
            Log.i("wislieZhu","这不是主进程..")
        }*/

        ANRWatchDog().start()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val file = File("/data/anr")
//            Log.i("wislieZhu","file 是否存在 ${file.exists()}")
            val fileObserver =   ANRFileObserver(file)
            fileObserver.startWatching()
        }

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
