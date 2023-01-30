package com.wislie.common.util.noleakdialog


import android.app.Dialog
import android.os.Bundle
import androidx.annotation.LayoutRes
import com.shehuan.nicedialog.ViewHolder


class NoLeakNiceDialog : BNiceDialog() {

    private var convertListener: NoLeakViewConvertListener? = null


    override fun intLayoutId() = this.layoutId
     override fun convertView(holder: ViewHolder?, dialog: BNiceDialog?) {
         convertListener?.let {
             convertListener?.convertView(holder, dialog)
         }
     }

    fun setConvertListener(convertListener: NoLeakViewConvertListener) {
        this.convertListener = convertListener
    }

    fun setLayout(@LayoutRes layoutId: Int) : NoLeakNiceDialog {
        this.layoutId = layoutId
        return this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            convertListener = savedInstanceState.getParcelable("listener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return if (context == null) super.onCreateDialog(savedInstanceState)
        else ProxyDialog(
            requireContext(),
            theme
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("listener", convertListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        convertListener = null
    }

    companion object {
        fun init(): NoLeakNiceDialog = NoLeakNiceDialog()
    }


}