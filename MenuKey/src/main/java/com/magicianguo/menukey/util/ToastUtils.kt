package com.magicianguo.menukey.util

import android.widget.Toast
import com.magicianguo.menukey.App

object ToastUtils {
    private val mContext = App.get()
    private var mToast: Toast? = null
    fun short(text: String) {
        mToast?.cancel()
        mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT)
        mToast?.show()
    }
}