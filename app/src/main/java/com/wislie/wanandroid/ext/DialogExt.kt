package com.wislie.wanandroid.ext

import android.util.Log
import android.view.Gravity
import android.view.View
import com.shehuan.nicedialog.ViewHolder
import com.wislie.common.base.BaseFragment
import com.wislie.common.util.noleakdialog.BNiceDialog
import com.wislie.common.util.noleakdialog.NoLeakNiceDialog
import com.wislie.common.util.noleakdialog.NoLeakViewConvertListener
import com.wislie.wanandroid.R

/**
 *    author : Wislie
 *    e-mail : 254457234@qq.comn
 *    date   : 2023/1/24 8:51 PM
 *    desc   : 对话框
 *    version: 1.0
 */

/**
 * 清理搜索历史
 */
fun BaseFragment<*>.clearSearchHistory(confirm:()->Unit){
    val frag = this
    activity?.let {
        if (!it.isFinishing) {
            if (loadingDialog === null) {
                loadingDialog = NoLeakNiceDialog.init()
            }
            loadingDialog?.run {
                if (!this.isAdded) {
                    setLayout(R.layout.layout_clear_search_history)
                    setGravity(Gravity.CENTER)
                    setMargin(30)
                    setOutCancel(true)
                    setConvertListener(object : NoLeakViewConvertListener() {
                        override fun convertView(holder: ViewHolder?, dialog: BNiceDialog?) {
                            holder?.run {
                                this.setOnClickListener(R.id.tv_clear_confirm, object : View.OnClickListener{
                                    override fun onClick(v: View?) {
                                        confirm()
                                        dialog?.run {
                                            dismiss()
                                        }
                                    }
                                })
                                this.setOnClickListener(R.id.tv_clear_cancel, object : View.OnClickListener{
                                    override fun onClick(v: View?) {
                                        dialog?.run {
                                            dismiss()
                                        }
                                    }
                                })
                            }
                        }
                    })
                    loadingDialog?.show(it.supportFragmentManager, frag.javaClass.simpleName)
                }
            }
        }
    }
}