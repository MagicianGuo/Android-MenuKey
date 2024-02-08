package com.magicianguo.menukey.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.WindowManager
import com.magicianguo.menukey.App
import com.magicianguo.menukey.constant.ViewOrientation
import com.magicianguo.menukey.view.MenuKeyView

object MenuKeyViewTools {
    private val mWindowManager = App.get().getSystemService(Context.WINDOW_SERVICE) as WindowManager
    @SuppressLint("StaticFieldLeak")
    private val mMenuKeyView = MenuKeyView(App.get())
    private val mMenuKeyViewParams = WindowManager.LayoutParams()
    var showMenuKey = false

    init {
        mMenuKeyViewParams.width = WindowManager.LayoutParams.WRAP_CONTENT
        mMenuKeyViewParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        mMenuKeyViewParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mMenuKeyViewParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            mMenuKeyViewParams.type = WindowManager.LayoutParams.TYPE_PHONE
        }
        mMenuKeyViewParams.gravity = Gravity.START or Gravity.TOP
        mMenuKeyViewParams.format = PixelFormat.TRANSLUCENT
        mMenuKeyViewParams.x = DimenUtils.dp2px(20F).toInt()
        mMenuKeyViewParams.y = DimenUtils.dp2px(100F).toInt()

        mMenuKeyView.setLayoutListener(object : MenuKeyView.ILayoutListener {
            override fun onLayout(x: Float, y: Float) {
                mMenuKeyViewParams.x = x.toInt()
                mMenuKeyViewParams.y = y.toInt()
                mWindowManager.updateViewLayout(mMenuKeyView, mMenuKeyViewParams)
            }
        })
    }

    fun addView() {
        if (!showMenuKey) {
            mWindowManager.addView(mMenuKeyView, mMenuKeyViewParams)
            showMenuKey = true
        }
    }

    fun removeView() {
        if (showMenuKey) {
            mWindowManager.removeView(mMenuKeyView)
            showMenuKey = false
        }
    }

    fun setOrientation(@ViewOrientation orientation: Int) {
        mMenuKeyView.configOrientation(orientation)
    }

    fun refreshLayout() {
        mMenuKeyView.refreshLayout()
    }

    fun setOrder(order: Int) {
        mMenuKeyView.addKeyView(order)
    }
}