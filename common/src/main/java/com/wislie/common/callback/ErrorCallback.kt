package com.wislie.common.callback

import com.kingja.loadsir.callback.Callback
import com.wislie.common.R

class ErrorCallback : Callback() {
    override fun onCreateView(): Int {
        return R.layout.layout_error
    }
}