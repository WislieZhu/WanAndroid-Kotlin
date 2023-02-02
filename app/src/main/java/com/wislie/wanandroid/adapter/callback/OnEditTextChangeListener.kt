package com.wislie.wanandroid.adapter.callback

import android.text.Editable
import com.google.android.material.textfield.TextInputEditText

interface OnEditTextChangeListener {

    fun afterTextChanged(textInputEditText: TextInputEditText, s: Editable?)
}