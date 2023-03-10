package com.wislie.wanandroid.test

import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.wislie.common.base.BaseFragment
import com.wislie.wanandroid.R
import com.wislie.wanandroid.databinding.FragmentTestBinding
import com.wislie.wanandroid.util.StudentCoroutine
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TestFragment : BaseFragment<FragmentTestBinding>() {
    override fun getLayoutResId(): Int {
        return R.layout.fragment_test
    }

    override fun init(root: View) {


        binding.btnTestXc.setOnClickListener {

            StudentCoroutine().getTeacher()
        }

        binding.btnXcNotBlock.setOnClickListener {
            lifecycleScope.launch {
                Log.i("wislieZhu", "${Thread.currentThread().name} start ")
                delay(2000)
                Log.i("wislieZhu", "${Thread.currentThread().name} end")
            }
            it.postDelayed({
                Log.i("wislieZhu", "${Thread.currentThread().name} 证明了协程不阻塞")
            }, 1000)




        }
    }


}