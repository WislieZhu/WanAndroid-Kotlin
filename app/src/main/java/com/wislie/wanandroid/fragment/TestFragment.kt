package com.wislie.wanandroid.fragment

import android.util.Log
import android.view.View
import com.wislie.common.base.BaseFragment
import com.wislie.wanandroid.R
import com.wislie.wanandroid.data.UserInfo
import com.wislie.wanandroid.databinding.FragmentTestBinding

class TestFragment:BaseFragment<FragmentTestBinding> (){
    override fun getLayoutResId(): Int {
        return R.layout.fragment_test
    }

    override fun init(root: View) {


        binding.btnTest.setOnClickListener {
            val userInfo = UserInfo(true, arrayListOf(), 1, arrayListOf(), "485537676@qq.com",
                "icon",11,"abc","123","bb","token",12,"zhu")
            Log.i("wislieZhu","userInfo=$userInfo")
        }
    }
}