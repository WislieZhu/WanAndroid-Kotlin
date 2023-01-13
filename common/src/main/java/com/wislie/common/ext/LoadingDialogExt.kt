package com.wislie.common.ext

import android.util.Log
import android.view.Gravity
import com.shehuan.nicedialog.ViewHolder
import com.wislie.common.R
import com.wislie.common.base.BaseActivity
import com.wislie.common.base.BaseFragment
import com.wislie.common.util.noleakdialog.BNiceDialog
import com.wislie.common.util.noleakdialog.NoLeakNiceDialog
import com.wislie.common.util.noleakdialog.NoLeakViewConvertListener


fun BaseFragment<*>.showLoading(loadingMessage: String = "请求网络中...", outCancel: Boolean = false) {
    val frag = this
    activity?.let {
        if (!it.isFinishing) {
            if (loadingDialog === null) {
                loadingDialog = NoLeakNiceDialog.init()
            }
            Log.i("wislieZhu", "BaseFragment showLoading name=${frag.javaClass.simpleName}")
            loadingDialog?.run {
                if (!this.isAdded) {
                    setLayout(R.layout.layout_custom_progress_dialog_view)
                    setGravity(Gravity.CENTER)
                    setMargin(30)
                    setOutCancel(outCancel)
                    setConvertListener(object : NoLeakViewConvertListener() {
                        override fun convertView(holder: ViewHolder?, dialog: BNiceDialog?) {
                            holder?.apply {
                                setText(R.id.loading_tips, loadingMessage)
                            }
                        }
                    })
                    loadingDialog?.show(it.supportFragmentManager, frag.javaClass.simpleName)
                }
            }
        }
    }
}

fun BaseFragment<*>.dismissLoading() {
    val frag = this
    activity?.run {
        loadingDialog?.run {
            Log.i("wislieZhu", "BaseFragment dismissLoading name=${frag.javaClass.simpleName}")
            dismiss()
        }
        loadingDialog = null
    }
}

fun BaseActivity<*>.showLoading(loadingMessage: String = "请求网络中...", outCancel: Boolean = false) {
    val act = this
    if (!isFinishing) {
        if (loadingDialog === null) {
            loadingDialog = NoLeakNiceDialog.init()
        }
        Log.i("wislieZhu", "BaseActivity showLoading name=${act.javaClass.simpleName}")
        loadingDialog?.run {
            if (!this.isAdded) {
                setLayout(R.layout.layout_custom_progress_dialog_view)
                setGravity(Gravity.CENTER)
                setMargin(30)
                setOutCancel(outCancel)
                setConvertListener(object : NoLeakViewConvertListener() {
                    override fun convertView(holder: ViewHolder?, dialog: BNiceDialog?) {
                        holder?.let {
                            it.setText(R.id.loading_tips, loadingMessage)
                        }
                    }
                })
                loadingDialog?.show(supportFragmentManager, act.javaClass.simpleName)
            }
        }
    }
}

fun BaseActivity<*>.dismissLoading() {
    val act = this
    loadingDialog?.run {
        Log.i("wislieZhu", "BaseActivity dismissLoading name=${act.javaClass.simpleName}")
        dismiss()
    }
    loadingDialog = null
}


