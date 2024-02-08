package com.magicianguo.menukey.view

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import com.magicianguo.menukey.R
import com.magicianguo.menukey.util.DimenUtils
import com.magicianguo.menukey.util.SPUtils

class KeyButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatImageView(context, attrs) {
    companion object {
        var buttonSizeDp = 0
        var buttonSizePx = 0
        fun setButtonSize(dpSize: Int) {
            buttonSizeDp = dpSize
            buttonSizePx = DimenUtils.dp2px(dpSize.toFloat()).toInt()
        }
    }

    init {
        setButtonSize(SPUtils.getButtonSize())
        setBackgroundResource(R.drawable.bg_key_view_item)
        alpha = 0.9F
    }

    fun setImage(@DrawableRes resId: Int): KeyButton {
        setImageResource(resId)
        return this
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(buttonSizePx, buttonSizePx)
    }
}