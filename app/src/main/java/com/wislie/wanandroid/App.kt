package com.wislie.wanandroid

//import com.github.moduth.blockcanary.BlockCanary
//import com.github.moduth.blockcanary.BlockCanaryInternals
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Debug
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
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


class App : Application() {

    val appViewModel: AppViewModel by lazy {
        AppViewModel(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
//        TimeMonitorManager.instance?.resetTimeMonitor(TimeMonitorConfig.TIME_MONITOR_ID_APPLICATION_START)
    }

    override fun onCreate() {
        super.onCreate()

        val file =  File(filesDir,"dmtrace.trace")
//        Log.i("wislieZhu","文件路径=${file.path}")

        Debug.startMethodTracing(file.path)

//        TimeMonitorManager.instance?.getTimeMonitor(TimeMonitorConfig.TIME_MONITOR_ID_APPLICATION_START)
//            ?.recordingTimeTag("Application-onCreate")
        instance = this
        Utils.init(this)

        MMKV.initialize(this)

        LoadSir.beginBuilder()
            .addCallback(ErrorCallback()) //错误页面
            .addCallback(LoadingCallback()) //加载页面
            .addCallback(EmptyCallback()) //空页面
            .setDefaultCallback(LoadingCallback::class.java) //设置默认加载状态页
            .commit()

        //todo 没展示
        handleSSLHandshake()


        /*if(Utils.isMainProcess(this)){
            Log.i("wislieZhu","这是主进程..")
            //注意在主进程初始化调用
            BlockCanary.install(this, AppBlockCanaryContext()).start()
        }else{
            Log.i("wislieZhu","这不是主进程..")
        }*/

        ANRWatchDog().start()
//        TimeMonitorManager.instance?.getTimeMonitor(TimeMonitorConfig.TIME_MONITOR_ID_APPLICATION_START)
//            ?.recordingTimeTag("Application-onCreate2")
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val file = File("/data/anr")
//            Log.i("wislieZhu","file 是否存在 ${file.exists()}")
            val fileObserver =   ANRFileObserver(file)
            fileObserver.startWatching()
        }*/

    }

    /**
     * 忽略https的证书校验
     */
    private fun handleSSLHandshake() {
        try {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate?> {
                    return arrayOfNulls<X509Certificate>(0)
                }

                override fun checkClientTrusted(certs: Array<X509Certificate?>?, authType: String?) {}
                override fun checkServerTrusted(certs: Array<X509Certificate?>?, authType: String?) {}
            })
            val sc = SSLContext.getInstance("TLS")
            // trustAllCerts信任所有的证书
            sc.init(null, trustAllCerts, SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
            HttpsURLConnection.setDefaultHostnameVerifier { hostname, session -> true }
        } catch (ignored: Exception) {
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
