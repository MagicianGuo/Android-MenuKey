package com.magicianguo.menukey.util

import com.magicianguo.menukey.App

object DimenUtils {
    private val mContext = App.get()

    fun dp2px(dp: Float): Float {
        val dpi = mContext.resources.configuration.densityDpi
        return dp * dpi / 160
    }

    fun getStatusBarHeight(): Int {
        val resourceId = mContext.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            return mContext.resources.getDimensionPixelSize(resourceId)
        }
        return 0
    }
}