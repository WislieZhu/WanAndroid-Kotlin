package com.wislie.wanandroid.fragment

import android.view.View
import com.wislie.common.base.BaseFragment
import com.wislie.wanandroid.R
import com.wislie.wanandroid.databinding.FragmentTestBinding

class TestFragment:BaseFragment<FragmentTestBinding> (){
    override fun getLayoutResId(): Int {
        return R.layout.fragment_test
    }

    override fun init(root: View) {

    }
}