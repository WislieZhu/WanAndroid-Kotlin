package com.wislie.common.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.wislie.common.util.LiveDataBusManager
import com.wislie.common.util.noleakdialog.NoLeakNiceDialog


abstract class BaseActivity<VB : ViewDataBinding> : AppCompatActivity() {

    lateinit var binding: VB
    var loadingDialog: NoLeakNiceDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isBindingLayout()) {
            binding = DataBindingUtil.setContentView(this, getLayoutResId())
            binding.lifecycleOwner = this
        } else {
            setContentView(getLayoutResId())
        }
        init(savedInstanceState)
        with(LiveDataBusManager.instance) {
            //监听对话框显示与关闭
            observe(this@BaseActivity, LiveDataBusManager.LOADING, ResultState.Loading::class.java)
        }
    }

    abstract fun getLayoutResId(): Int

    open fun init(savedInstanceState: Bundle?) {}

    open fun isBindingLayout() = true
}

val BaseActivity<*>.TAG: String get() = this::class.java.simpleName