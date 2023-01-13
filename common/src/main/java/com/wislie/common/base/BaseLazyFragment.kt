package com.wislie.common.base

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle

abstract class BaseLazyFragment<VB : ViewDataBinding> : BaseFragment<VB>() {

    /**
     * false表示已加载过, true表示未加载过
     */
    private var isLoaded = true

    override fun onResume() {
        super.onResume()
        if (lifecycle.currentState == Lifecycle.State.STARTED && isLoaded) {
            loadData()
            isLoaded = false
        }
    }

    abstract fun loadData()
}