package com.wislie.common.util.noleakdialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface

class ProxyDialog : Dialog {

    constructor(context: Context) : super(context)
    constructor(context: Context, theme: Int) : super(context, theme)
    constructor(
        context: Context,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener
    ) : super(context, cancelable, cancelListener)

    override fun setOnCancelListener(listener: DialogInterface.OnCancelListener?) {
        super.setOnCancelListener(DialogInterfaceProxy.Companion.ProxyOnCancelListener(listener))
    }

    override fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        super.setOnDismissListener(DialogInterfaceProxy.Companion.ProxyOnDismissListener(listener))
    }

    override fun setOnShowListener(listener: DialogInterface.OnShowListener?) {
        super.setOnShowListener(DialogInterfaceProxy.Companion.ProxyOnShowListener(listener))
    }

}