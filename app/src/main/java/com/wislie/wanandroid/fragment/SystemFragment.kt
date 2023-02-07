package com.wislie.wanandroid.fragment

import android.util.Log
import android.view.View
import com.wislie.common.base.BaseViewModel
import com.wislie.common.base.BaseViewModelFragment
import com.wislie.wanandroid.R
import com.wislie.wanandroid.databinding.FragmentSystemBinding

/**
 * 体系
 */
class SystemFragment:BaseViewModelFragment<BaseViewModel, FragmentSystemBinding>() {
    override fun getLayoutResId(): Int {
        return R.layout.fragment_system
    }

    override fun init(root: View) {
        super.init(root)

    }

    override fun loadData() {
        Log.i("wislieZhu","SystemFragment..")
    }

    override fun observeData() {

    }


}