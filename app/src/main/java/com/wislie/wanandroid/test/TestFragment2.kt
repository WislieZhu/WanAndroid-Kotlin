package com.wislie.wanandroid.test

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.wislie.common.base.BaseFragment
import com.wislie.wanandroid.R
import com.wislie.wanandroid.databinding.FragmentTest2Binding
import com.wislie.wanandroid.databinding.FragmentTestBinding
import com.wislie.wanandroid.util.StudentCoroutine
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TestFragment2 : BaseFragment<FragmentTest2Binding>() {
    override fun getLayoutResId(): Int {
        return R.layout.fragment_test_2
    }



    override fun init(root: View) {
        arguments?.getString("content")?.run {
            binding.tvContent.text = this
        }
    }



}

