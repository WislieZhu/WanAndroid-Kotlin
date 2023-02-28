package com.wislie.common.base

import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.wislie.common.util.KeyboardUtil
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
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {

        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                currentFocus?.run {
                    if(KeyboardUtil.shouldHideInputMethod(this,ev)){
                        KeyboardUtil.closeSoftKeyboard(this)
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    abstract fun getLayoutResId(): Int

    open fun init(savedInstanceState: Bundle?) {}

    open fun isBindingLayout() = true
}

val BaseActivity<*>.TAG: String get() = this::class.java.simpleName