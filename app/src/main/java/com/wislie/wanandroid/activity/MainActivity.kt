package com.wislie.wanandroid.activity

//import androidx.core.splashscreen.SplashScreen
//import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import android.Manifest
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.FrameMetrics
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.wislie.common.base.BaseActivity
import com.wislie.wanandroid.R
import com.wislie.wanandroid.databinding.ActivityMainBinding


class MainActivity : BaseActivity<ActivityMainBinding>() {




    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        //设置修改状态栏
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        //设置状态栏的颜色，和你的APP主题或者标题栏颜色一致就可以了
        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_500)
        //        installSplashScreen()


        super.onCreate(savedInstanceState)

        val list = ArrayList<String>()
        list.add("a")
        test1(list)

//        val listA = mutableListOf<String>()
//        test2(listA)





//        Looper.getMainLooper().setMessageLogging()
//        LooperPrinter

       /* var sumFrameCost = 0L
        var sumFrames = 0L
        var lastDuration = 0L
        var lastFrames = 0L
        val frameIntervalNanos = 1 / windowManager.defaultDisplay.refreshRate * 1000000000
        window.addOnFrameMetricsAvailableListener({ window, frameMetrics, dropCountSinceLastInvocation ->
            val frameMetricsCopy = FrameMetrics(frameMetrics)
            val vsyncTime = frameMetricsCopy.getMetric(FrameMetrics.VSYNC_TIMESTAMP)
            val intentedVsyncTime = frameMetricsCopy.getMetric(FrameMetrics.INTENDED_VSYNC_TIMESTAMP)
            val jitter = vsyncTime - intentedVsyncTime
            val dropFrame: Int = (jitter / frameIntervalNanos).toInt()

            // 不丢帧时正常帧也要算进去所以要+1
            sumFrameCost += ((dropFrame + 1) * frameIntervalNanos / 1000000).toLong()
            sumFrames += 1

            val duration = sumFrameCost - lastDuration
            val collectFrame = sumFrames - lastFrames

            if (duration >= 200) {
                val fps = 1000.0f * collectFrame / duration
                Log.i("wislieZhu", ">>>>>>>fps->$fps")
                lastFrames = sumFrames
                lastDuration = sumFrameCost
            }
        }, Handler(Looper.getMainLooper()))*/



    }


    override fun getLayoutResId(): Int = R.layout.activity_main


    private fun test1(list: MutableList<String>) {

    }

    private fun test2(list: ArrayList<String>) {

    }

//    Arraylist -> MutableList

}

