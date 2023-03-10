package com.wislie.common.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import java.util.*

object DeviceUtil {

    @SuppressLint("HardwareIds")
    fun getDeviceId(): String {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            val telephonyManager = Utils.getApp().getSystemService(Context.TELEPHONY_SERVICE)
            if (telephonyManager != null && telephonyManager is TelephonyManager) {
                return telephonyManager.deviceId
            }

        }
        //ANDROID_ID 恢复出厂设置,ANDROID_ID会发生改变
        return Settings.Secure.getString(
            Utils.getApp().applicationContext.contentResolver,
            Settings.Secure.ANDROID_ID
        )

    }

    /**
     * 获取独一无二的设备Id
     */
    fun getDeviceUniqueId():String{
        val devIdShort =
            "35" + Build.BOARD.length % 10 + Build.BRAND.length % 10 + Build.CPU_ABI.length % 10 +
                    Build.DEVICE.length % 10 + Build.DISPLAY.length % 10 + Build.HOST.length % 10 +
                    Build.ID.length % 10 + Build.MANUFACTURER.length % 10 + Build.MODEL.length % 10 +
                    Build.PRODUCT.length % 10 + Build.TAGS.length % 10 + Build.TYPE.length % 10 +
                    Build.USER.length % 10 //13 位
        var serial = try {
            Build::class.java.getField("SERIAL")[null].toString()
        } catch (exception: Exception) {
            "serial" // 随便一个初始化
        }
        Log.i("wislieZhu","serial=$serial devIdShort=$devIdShort")
        //serial=EMULATOR31X3X13X0 devIdShort=357631353650243 android7 emulator
        //serial=unknown devIdShort=355666525699893 android13 emulator
        //serial=unknown devIdShort=353595334688244 honor20
        return UUID(devIdShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
    }
}