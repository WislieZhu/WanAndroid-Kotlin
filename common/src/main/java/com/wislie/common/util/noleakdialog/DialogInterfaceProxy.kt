package com.wislie.common.util.noleakdialog

import android.content.DialogInterface
import java.lang.ref.WeakReference

class DialogInterfaceProxy {


    companion object {

        /**
         * 使用弱引用持有Dialog真正的DialogInterface.OnCancelListener,
         * <p>
         * 即使Message.obj未被正常释放，最终它持有的也只是ProxyOnCancelListener的一个空壳实例
         */
        class ProxyOnCancelListener(onCancelListener: DialogInterface.OnCancelListener?) :
            DialogInterface.OnCancelListener {
            private val mProxyRef: WeakReference<DialogInterface.OnCancelListener?> =
                WeakReference(onCancelListener)

            override fun onCancel(dialog: DialogInterface?) {
                val onCancelListener = mProxyRef.get()
                onCancelListener?.let {
                    it.onCancel(dialog)
                }
            }
        }

        /**
         * 使用弱引用持有Dialog真正的DialogInterface.OnDismissListener,
         * <p>
         * 即使Message.obj未被正常释放，最终它持有的也只是ProxyOnDismissListener的一个空壳实例
         */
        class ProxyOnDismissListener(onDismissListener: DialogInterface.OnDismissListener?) :
            DialogInterface.OnDismissListener {
            private val mProxyRef: WeakReference<DialogInterface.OnDismissListener?> =
                WeakReference(onDismissListener)

            override fun onDismiss(dialog: DialogInterface?) {
                val onDismissListener = mProxyRef.get()
                onDismissListener?.let {
                    it.onDismiss(dialog)
                }
            }
        }

        /**
         * 使用弱引用持有Dialog真正的DialogInterface.OnShowListener,
         * <p>
         * 即使Message.obj未被正常释放，最终它持有的也只是ProxyOnShowListener的一个空壳实例
         */
        class ProxyOnShowListener(onShowListener: DialogInterface.OnShowListener?) :
            DialogInterface.OnShowListener {
            private val mProxyRef: WeakReference<DialogInterface.OnShowListener?> =
                WeakReference(onShowListener)

            override fun onShow(dialog: DialogInterface?) {
                val onShowListener = mProxyRef.get()
                onShowListener?.let {
                    onShowListener.onShow(dialog)
                }
            }
        }

    }
}