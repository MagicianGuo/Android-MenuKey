package com.magicianguo.menukey.util

import android.view.KeyEvent
import com.magicianguo.menukey.service.KeyInputService

object KeyInputServiceTools {
    private var mService: KeyInputService? = null

    fun setService(service: KeyInputService?) {
        mService = service
    }

    fun clickKey(keyCode: Int) {
        val service = mService
        if (service != null) {
            val inputConnection = service.currentInputConnection
            inputConnection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, keyCode))
            inputConnection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, keyCode))
        }
    }
}