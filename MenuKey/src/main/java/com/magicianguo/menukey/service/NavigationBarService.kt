package com.magicianguo.menukey.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.magicianguo.menukey.util.NavigationBarServiceTools
import com.magicianguo.menukey.util.PermissionTools

class NavigationBarService : AccessibilityService() {
    override fun onCreate() {
        super.onCreate()
        PermissionTools.isAccessibilityEnabled = true
        NavigationBarServiceTools.setService(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        PermissionTools.isAccessibilityEnabled = false
        NavigationBarServiceTools.setService(null)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
    }

    override fun onInterrupt() {
    }
}