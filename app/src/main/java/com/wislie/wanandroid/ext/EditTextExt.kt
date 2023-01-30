package com.wislie.wanandroid.ext

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

/**
 *    author : Wislie
 *    e-mail : 254457234@qq.comn
 *    date   : 2023/1/23 7:44 PM
 *    desc   :
 *    version: 1.0
 */

fun EditText.addTextListener(
    etBeforeTextChanged: ((CharSequence?, Int, Int, Int) -> Unit)?=null,
    etOnTextChanged: ((CharSequence?, Int, Int, Int) -> Unit)?=null,
    etAfterTextChanged: ((Editable?) -> Unit)?=null
) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {
            etBeforeTextChanged?.invoke(s, start, count, after)
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            etOnTextChanged?.invoke(s, start, before, count)
        }

        override fun afterTextChanged(s: Editable?) {
            etAfterTextChanged?.invoke(s)
        }
    })
}