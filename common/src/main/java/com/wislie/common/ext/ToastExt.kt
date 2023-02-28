package com.wislie.common.ext

import android.content.Context
import android.widget.Toast

fun Context.showToast(content: String) =
    Toast.makeText(this, content, Toast.LENGTH_SHORT).show()