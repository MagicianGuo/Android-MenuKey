package com.magicianguo.menukey.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import com.magicianguo.menukey.R
import com.magicianguo.menukey.constant.RequestCode


object PermissionTools {
    var isAccessibilityEnabled = false
    fun checkNotificationPermission(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (activity.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    RequestCode.NOTIFICATION
                )
            }
        }
    }

    fun hasAlertPermission(context: Context): Boolean {
        return Settings.canDrawOverlays(context)
    }

    fun requestAlertPermission(activity: Activity) {
        ToastUtils.short("请选择“${activity.getString(R.string.app_name)}”")
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            .setData(Uri.parse("package:${activity.packageName}"))
        activity.startActivityForResult(intent, RequestCode.ALERT)
    }

    fun requestEnableAccessibility(activity: Activity) {
        ToastUtils.short("请选择“${activity.getString(R.string.service_navigation_bar_label)}”")
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        activity.startActivityForResult(intent, RequestCode.ACCESSIBILITY)
    }

    fun isCurrentIMEnabled(context: Context): Boolean {
        val manager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.enabledInputMethodList.forEach {
            if (it.packageName == context.packageName) {
                return true
            }
        }
        return false
    }

    fun requestEnableCurrentIM(activity: Activity) {
        ToastUtils.short("请开启“${activity.getString(R.string.app_name)}”")
        val intent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)
        activity.startActivityForResult(intent, RequestCode.IM_ENABLE)
    }

    fun isCurrentIMSelected(context: Context): Boolean {
        val name = Settings.Secure.getString(context.contentResolver, Settings.Secure.DEFAULT_INPUT_METHOD)
        return name.contains("${context.packageName}/")
    }

    fun requestSelectIM(activity: Activity) {
        val manager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.showInputMethodPicker()
    }
}