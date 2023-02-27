package com.wislie.wanandroid.viewmodel

import android.text.Editable
import android.widget.EditText
import androidx.databinding.ObservableField
import com.wislie.common.base.BaseViewModel
import com.wislie.wanandroid.adapter.callback.OnEditTextChangeListener

/**
 * 微信公众号
 */
class WxArticleStateViewModel : BaseViewModel() {


    //输入的内容
    val inputContent: ObservableField<String> by lazy {
        ObservableField("")
    }
    val onInputEditTextChangeListener: OnEditTextChangeListener = object : OnEditTextChangeListener {
        override fun afterTextChanged(textInputEditText: EditText, s: Editable?) {
            val k = s?.run {
                this.toString()
            }
            inputContent.set(k)
        }
    }

}