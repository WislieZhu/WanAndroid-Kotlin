package com.wislie.wanandroid.viewmodel


import android.text.Editable
import android.widget.EditText
import androidx.databinding.ObservableField
import com.wislie.common.base.BaseViewModel
import com.wislie.wanandroid.adapter.callback.OnEditTextChangeListener

/**
 * 分享相关
 */
class ShareStateViewModel : BaseViewModel() {


    //输入的标题
    val title: ObservableField<String> by lazy {
        ObservableField("")
    }

    //输入的链接
    val link: ObservableField<String> by lazy {
        ObservableField("")
    }

    //输入标题监听
    val onTitleEditTextChangeListener = object : OnEditTextChangeListener {
        override fun afterTextChanged(textInputEditText: EditText, s: Editable?) {
            title.set(s?.toString())
        }
    }

    //输入链接监听
    val onLinkEditTextChangeListener = object : OnEditTextChangeListener {
        override fun afterTextChanged(textInputEditText: EditText, s: Editable?) {
            link.set(s?.toString())
        }
    }


}