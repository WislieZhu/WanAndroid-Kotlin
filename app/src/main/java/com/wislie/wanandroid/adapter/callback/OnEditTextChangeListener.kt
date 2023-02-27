package com.wislie.wanandroid.adapter.callback

import android.text.Editable
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText

interface OnEditTextChangeListener {

    fun afterTextChanged(textInputEditText: EditText, s: Editable?)
}