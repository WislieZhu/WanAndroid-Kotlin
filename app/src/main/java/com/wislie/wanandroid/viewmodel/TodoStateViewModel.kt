package com.wislie.wanandroid.viewmodel


import android.text.Editable
import android.widget.EditText
import android.widget.RadioGroup
import androidx.databinding.ObservableField
import com.wislie.common.base.BaseViewModel
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.callback.OnEditTextChangeListener

/**
 * todo相关
 */
class TodoStateViewModel : BaseViewModel() {


    //输入的标题
    val title: ObservableField<String> by lazy {
        ObservableField("")
    }

    //输入的内容
    val content: ObservableField<String> by lazy {
        ObservableField("")
    }

    //选择的日期
    val date: ObservableField<String> by lazy {
        ObservableField("")
    }

    //类型
    val type: ObservableField<Int> by lazy {
        ObservableField()
    }

    //优先级
    val priority: ObservableField<Int> by lazy {
        ObservableField()
    }

    //输入标题监听
    val onTitleEditTextChangeListener = object : OnEditTextChangeListener {
        override fun afterTextChanged(textInputEditText: EditText, s: Editable?) {
            title.set(s?.toString())
        }
    }

    //输入内容监听
    val onContentEditTextChangeListener = object : OnEditTextChangeListener {
        override fun afterTextChanged(textInputEditText: EditText, s: Editable?) {
            content.set(s?.toString())
        }
    }

    //类型
    val onTypeCheckedChangeListener =
        RadioGroup.OnCheckedChangeListener { _, checkedId ->
            if (R.id.rb_job == checkedId) {
                type.set(1)
            } else if (R.id.rb_life == checkedId) {
                type.set(2)
            } else if (R.id.rb_entertainment == checkedId) {
                type.set(3)
            }
        }


    //优先级
    val onPriorityCheckedChangeListener =
        RadioGroup.OnCheckedChangeListener { _, checkedId ->
            if (R.id.rb_import == checkedId) {
                priority.set(1)
            } else if (R.id.rb_generate == checkedId) {
                priority.set(2)
            }
        }
}