package com.wislie.wanandroid.viewmodel

import android.text.Editable
import android.view.View
import androidx.databinding.ObservableField
import com.google.android.material.textfield.TextInputEditText
import com.wislie.common.base.BaseViewModel
import com.wislie.wanandroid.adapter.callback.OnEditTextChangeListener
import com.wislie.wanandroid.ext.getInputLayout
import com.wislie.wanandroid.ext.setStartIconColor

/**
 * 登录
 */
class LoginStateViewModel : BaseViewModel() {


    //输入的账号
    val account: ObservableField<String> by lazy {
        ObservableField("")
    }

    //输入的密码
    val password: ObservableField<String> by lazy {
        ObservableField("")
    }

    //输入的密码
    val confirmPassword: ObservableField<String> by lazy {
        ObservableField("")
    }

    //实现对TextInputEditText的焦点监听, 使得TextInputLayout的startIcon 颜色发生变化
    val onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
        if (v is TextInputEditText?) {
            v?.run {
                this.getInputLayout()?.run {
                    setStartIconColor(hasFocus)
                }
            }

        }
    }

    //输入账号监听
    val onAccountEditTextChangeListener = object : OnEditTextChangeListener {
        override fun afterTextChanged(textInputEditText: TextInputEditText, s: Editable?) {
            textInputEditText.getInputLayout()?.run {
                val len = s?.toString()?.length ?: 0
                error = if (len > 10) {
                    "账号长度超出限制"
                } else {
                    null
                }
            }
            account.set(s?.toString())
        }
    }

    //输入密码监听
    val onPasswordEditTextChangeListener = object : OnEditTextChangeListener {
        override fun afterTextChanged(textInputEditText: TextInputEditText, s: Editable?) {
            password.set(s?.toString())
        }
    }

    //输入确认密码监听
    val onConfirmPasswordEditTextChangeListener = object : OnEditTextChangeListener {
        override fun afterTextChanged(textInputEditText: TextInputEditText, s: Editable?) {
            confirmPassword.set(s?.toString())
        }
    }
}