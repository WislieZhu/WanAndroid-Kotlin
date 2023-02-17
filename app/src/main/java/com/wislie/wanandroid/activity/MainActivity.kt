package com.wislie.wanandroid.activity

//import androidx.core.splashscreen.SplashScreen
//import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.wislie.common.base.BaseActivity
import com.wislie.wanandroid.R
import com.wislie.wanandroid.databinding.ActivityMainBinding


class MainActivity : BaseActivity<ActivityMainBinding>() {


    val permissions =
        arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

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



        ActivityCompat.requestPermissions(
            this, permissions,
            100
        )

    }


    override fun getLayoutResId(): Int = R.layout.activity_main


    private fun test1(list: MutableList<String>) {

    }

    private fun test2(list: ArrayList<String>) {

    }

//    Arraylist -> MutableList

}

