package com.magicianguo.menukey.util

import com.magicianguo.menukey.service.NavigationBarService

object NavigationBarServiceTools {
    private var mService: NavigationBarService? = null
    fun setService(service: NavigationBarService?) {
        mService = service
    }

    fun performAction(action: Int) {
        mService?.performGlobalAction(action)
    }
}