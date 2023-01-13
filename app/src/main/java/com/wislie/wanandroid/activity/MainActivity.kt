package com.wislie.wanandroid.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
//import androidx.core.splashscreen.SplashScreen
//import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.wislie.common.base.BaseActivity
import com.wislie.wanandroid.R
import com.wislie.wanandroid.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext


class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //设置修改状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            //设置状态栏的颜色，和你的APP主题或者标题栏颜色一致就可以了
            window.statusBarColor = ContextCompat.getColor(this, R.color.purple_500)
        }
//        installSplashScreen()
        super.onCreate(savedInstanceState)
    }


    override fun getLayoutResId(): Int = R.layout.activity_main

}