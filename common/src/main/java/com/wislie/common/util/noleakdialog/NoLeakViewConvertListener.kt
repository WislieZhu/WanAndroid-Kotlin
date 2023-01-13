package com.wislie.common.util.noleakdialog

import android.os.Parcel
import android.os.Parcelable
import com.shehuan.nicedialog.ViewHolder


abstract class NoLeakViewConvertListener : Parcelable{

    constructor()
    abstract fun convertView(holder: ViewHolder?, dialog: com.wislie.common.util.noleakdialog.BNiceDialog?)
    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {

    }

    companion object CREATOR : Parcelable.Creator<NoLeakViewConvertListener> {
        override fun createFromParcel(parcel: Parcel): NoLeakViewConvertListener {
            return object : NoLeakViewConvertListener() {

                override fun convertView(holder: ViewHolder?, dialog: com.wislie.common.util.noleakdialog.BNiceDialog?) {

                }
            }
        }

        override fun newArray(size: Int): Array<NoLeakViewConvertListener?> {
            return arrayOfNulls(size)
        }
    }
}