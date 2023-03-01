package com.wislie.common.util

import android.os.Parcelable
import com.tencent.mmkv.MMKV
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class Pref<T>(private val defaultValue: T) : ReadWriteProperty<Any?, T> {

    private val pref by lazy {
        MMKV.defaultMMKV()
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return when (defaultValue) {
            is ByteArray -> pref.decodeBytes(property.name, defaultValue)
            is String -> pref.decodeString(property.name, defaultValue)
            is Boolean -> pref.decodeBool(property.name, defaultValue)
            is Int -> pref.decodeInt(property.name, defaultValue)
            is Float -> pref.decodeFloat(property.name, defaultValue)
            is Double -> pref.decodeDouble(property.name, defaultValue)
            is Long -> pref.decodeLong(property.name, defaultValue)
            is Int? -> pref.decodeInt(property.name)
            is String? -> pref.decodeString(property.name)
            is Parcelable -> pref.decodeParcelable(property.name, defaultValue.javaClass)
            is Set<*> -> pref.decodeStringSet(property.name)
            else -> throw IllegalArgumentException("Unsupported type.")
        } as T
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        when (value) {
            is ByteArray -> pref.encode(property.name, value)
            is String -> pref.encode(property.name, value)
            is Boolean -> pref.encode(property.name, value)
            is Int -> pref.encode(property.name, value)
            is Float -> pref.encode(property.name, value)
            is Double -> pref.encode(property.name, value)
            is Long -> pref.encode(property.name, value)
            is String? -> pref.encode(property.name, value)
            is Parcelable -> pref.encode(property.name, value)
            is Set<*> -> pref.encode(property.name, value as MutableSet<String>)
            else -> throw IllegalArgumentException("Unsupported type.")
        }
    }

}