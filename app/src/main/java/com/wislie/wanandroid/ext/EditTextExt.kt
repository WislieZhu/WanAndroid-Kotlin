package com.wislie.wanandroid.ext

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.google.android.material.internal.CheckableImageButton
import com.google.android.material.textfield.TextInputLayout
import com.wislie.wanandroid.R

/**
 *    author : Wislie
 *    e-mail : 254457234@qq.comn
 *    date   : 2023/1/23 7:44 PM
 *    desc   :
 *    version: 1.0
 */

fun EditText.addTextListener(
    etBeforeTextChanged: ((CharSequence?, Int, Int, Int) -> Unit)? = null,
    etOnTextChanged: ((CharSequence?, Int, Int, Int) -> Unit)? = null,
    etAfterTextChanged: ((Editable?) -> Unit)? = null
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

/**
 * 监听EditText处于焦点
 */
fun EditText.addFocusListener(action: (Boolean) -> Unit) {
    setOnFocusChangeListener { _, hasFocus ->
        action.invoke(hasFocus)
    }
}

/**
 * EditText 长度不为0 返回true, 否则返回false
 */
fun EditText?.len():Boolean  {
    return this?.let {
        it.length() != 0
    } ?: false
}

/**
 * 设置TextInputLayout的左侧图标颜色
 */
fun TextInputLayout.setStartIconColor(hasFocus: Boolean) {
    val color = if (hasFocus) ContextCompat.getColor(
        context,
        R.color.purple_500
    ) else ContextCompat.getColor(context, R.color.color_B6B6B6)
    setStartIconTintList(ColorStateList.valueOf(color))
}

/**
 * 设置TextInputLayout的左侧图标颜色
 */
fun TextInputLayout.setPasswordIconColor(isChecked: Boolean) {
    val color = if (isChecked) ContextCompat.getColor(
        context,
        R.color.purple_500
    ) else ContextCompat.getColor(context, R.color.color_B6B6B6)
    setEndIconTintList(ColorStateList.valueOf(color))
}

/**
 * 设置TextInputLayout布局中的TextInputEditText 密码是否可见
 */
@SuppressLint("RestrictedApi")
fun TextInputLayout.setPasswordTransformation() {
    setEndIconOnClickListener { endIcon ->
        if (endIcon is CheckableImageButton) {
            editText?.run {
                val checked = !endIcon.isChecked

                //设置显示隐藏密码的按钮颜色
                setPasswordIconColor(checked)

                //设置显示隐藏密码
                transformationMethod = if (checked) {
                    HideReturnsTransformationMethod.getInstance()
                } else {
                    PasswordTransformationMethod.getInstance()
                }
                setSelection(this.length())
            }
        }
    }
}

/**
 * 清空EditText中的内容
 */
fun TextInputLayout.clearEditText() {
    setEndIconOnClickListener {
       editText?.run {
          setText("")
       }
    }
}

/**
 * 设置TextInputLayout布局中的最后一个按钮是否可见
 */
fun TextInputLayout.setEndIconTransformation(action:(EditText?)->Boolean){
    isEndIconVisible  = action.invoke(editText)
}