package com.wislie.common.callback

import android.content.Context
import android.view.View
import com.kingja.loadsir.callback.Callback
import com.wislie.common.R

class LoadingCallback: Callback() {
    override fun onCreateView() = R.layout.layout_loading

    override fun onReloadEvent(context: Context?, view: View?): Boolean {
        return true
    }
}