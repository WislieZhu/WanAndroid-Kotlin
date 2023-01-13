package com.wislie.wanandroid.fragment

import android.view.View
import com.wislie.common.base.BaseViewModel
import com.wislie.common.base.BaseViewModelFragment
import com.wislie.wanandroid.R
import com.wislie.wanandroid.databinding.FragmentPublicBinding

/**
 * 公共的
 */
class PublicFragment:BaseViewModelFragment<BaseViewModel, FragmentPublicBinding>() {
    override fun getLayoutResId(): Int {
        return R.layout.fragment_public
    }

    override fun init(root: View) {

    }

    override fun loadData() {

    }

    override fun observeData() {

    }


}