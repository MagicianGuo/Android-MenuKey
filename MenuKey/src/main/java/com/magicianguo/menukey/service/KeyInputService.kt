package com.magicianguo.menukey.service

import android.inputmethodservice.InputMethodService
import com.magicianguo.menukey.util.KeyInputServiceTools

class KeyInputService : InputMethodService() {
    override fun onCreate() {
        super.onCreate()
        KeyInputServiceTools.setService(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        KeyInputServiceTools.setService(null)
    }
}