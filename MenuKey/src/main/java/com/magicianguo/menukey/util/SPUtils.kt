package com.magicianguo.menukey.util

import android.content.Context
import com.magicianguo.menukey.App
import com.magicianguo.menukey.constant.ViewOrientation

object SPUtils {
    private val mSP = App.get().getSharedPreferences("SPUtils", Context.MODE_PRIVATE)
    private const val KEY_VIEW_ORIENTATION = "KEY_VIEW_ORIENTATION"
    private const val KEY_BUTTON_SIZE = "KEY_BUTTON_SIZE"
    private const val KEY_ORDER = "KEY_ORDER"

    fun setOrientation(orientation: Int) {
        val edit = mSP.edit()
        edit.putInt(KEY_VIEW_ORIENTATION, orientation)
        edit.apply()
    }

    fun getOrientation(): Int {
        return mSP.getInt(KEY_VIEW_ORIENTATION, ViewOrientation.VERTICAL)
    }

    fun setButtonSize(size: Int) {
        val edit = mSP.edit()
        edit.putInt(KEY_BUTTON_SIZE, size)
        edit.apply()
    }

    fun getButtonSize(): Int {
        return mSP.getInt(KEY_BUTTON_SIZE, 40)
    }

    fun setOrder(order: Int) {
        val edit = mSP.edit()
        edit.putInt(KEY_ORDER, order)
        edit.apply()
    }

    fun getOrder(): Int {
        return mSP.getInt(KEY_ORDER, 0x1234)
    }
}