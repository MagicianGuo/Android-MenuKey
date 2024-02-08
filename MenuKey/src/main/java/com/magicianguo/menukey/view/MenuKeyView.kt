package com.magicianguo.menukey.view

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import com.magicianguo.menukey.R
import com.magicianguo.menukey.constant.KeyOrder
import com.magicianguo.menukey.constant.ViewOrientation
import com.magicianguo.menukey.util.DimenUtils
import com.magicianguo.menukey.util.KeyInputServiceTools
import com.magicianguo.menukey.util.NavigationBarServiceTools
import com.magicianguo.menukey.util.SPUtils
import kotlin.math.abs

class MenuKeyView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    companion object {
        private val START_MOVE_OFFSET = DimenUtils.dp2px(5F)
        private val STATUS_BAR_HEIGHT = DimenUtils.getStatusBarHeight()
    }
    private var mButtonDownX = 0F
    private var mButtonDownY = 0F
    private var mRootDownX = 0F
    private var mRootDownY = 0F
    private var mIsMoving = false
    private var mIsPressing = false
    private val mIvBack: View
    private val mIvHome: View
    private val mIvRecent: View
    private val mIvMenu: View
    private var mKeyOrder = SPUtils.getOrder()

    private val mOnTouchListener: OnTouchListener

    init {
        setBackgroundResource(R.drawable.bg_key_view)
        configOrientation(SPUtils.getOrientation())

        mIvBack = KeyButton(context, attrs).setImage(R.mipmap.ic_back)
        mIvHome = KeyButton(context, attrs).setImage(R.mipmap.ic_home)
        mIvRecent = KeyButton(context, attrs).setImage(R.mipmap.ic_recent)
        mIvMenu = KeyButton(context, attrs).setImage(R.mipmap.ic_menu)

        addKeyView(mKeyOrder)

        mOnTouchListener = object : OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                resolveButtonTouchEvent(v, event)
                return true
            }
        }

        mIvBack.setOnTouchListener(mOnTouchListener)
        mIvHome.setOnTouchListener(mOnTouchListener)
        mIvRecent.setOnTouchListener(mOnTouchListener)
        mIvMenu.setOnTouchListener(mOnTouchListener)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            mRootDownX = ev.x
            mRootDownY = ev.y
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun resolveButtonTouchEvent(v: View, event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mButtonDownX = event.x
                mButtonDownY = event.y
                mIsMoving = false
                mIsPressing = true
                v.isPressed = true
            }
            MotionEvent.ACTION_MOVE -> {
                mIsMoving = mIsMoving || abs(event.x - mButtonDownX) > START_MOVE_OFFSET || abs(event.y - mButtonDownY) > START_MOVE_OFFSET
                if (mIsMoving) {
                    val x = event.rawX - mRootDownX
                    val y = event.rawY - mRootDownY - STATUS_BAR_HEIGHT
                    mListener?.onLayout(x, y)
                    if (mIsPressing) {
                        v.isPressed = false
                        mIsPressing = false
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                if (!mIsMoving) {
                    when (v) {
                        mIvBack -> NavigationBarServiceTools.performAction(AccessibilityService.GLOBAL_ACTION_BACK)
                        mIvHome -> NavigationBarServiceTools.performAction(AccessibilityService.GLOBAL_ACTION_HOME)
                        mIvRecent -> NavigationBarServiceTools.performAction(AccessibilityService.GLOBAL_ACTION_RECENTS)
                        mIvMenu -> KeyInputServiceTools.clickKey(KeyEvent.KEYCODE_MENU)
                    }
                }
                v.isPressed = false
            }
        }
    }

    fun configOrientation(@ViewOrientation newOrientation: Int) {
        orientation = when (newOrientation) {
            ViewOrientation.VERTICAL -> VERTICAL
            else -> HORIZONTAL
        }
    }

    fun refreshLayout() {
        for (i in 0 until childCount) {
            getChildAt(i).requestLayout()
        }
    }

    fun addKeyView(order: Int) {
        val order1 = order and KeyOrder.MASK_ORDER_1 shr 12
        val order2 = order and KeyOrder.MASK_ORDER_2 shr 8
        val order3 = order and KeyOrder.MASK_ORDER_3 shr 4
        val order4 = order and KeyOrder.MASK_ORDER_4
        addKeyView(order1, order2, order3, order4)
    }

    private fun addKeyView(order1: Int, order2: Int, order3: Int, order4: Int) {
        removeAllViews()
        addKeyViewByOrder(order1)
        addKeyViewByOrder(order2)
        addKeyViewByOrder(order3)
        addKeyViewByOrder(order4)
    }

    private fun addKeyViewByOrder(order: Int) {
        when (order) {
            KeyOrder.ORDER_BACK -> addView(mIvBack)
            KeyOrder.ORDER_HOME -> addView(mIvHome)
            KeyOrder.ORDER_RECENT -> addView(mIvRecent)
            KeyOrder.ORDER_MENU -> addView(mIvMenu)
        }
    }

    private var mListener: ILayoutListener? = null

    fun setLayoutListener(listener: ILayoutListener) {
        mListener = listener
    }

    interface ILayoutListener {
        fun onLayout(x: Float, y: Float)
    }
}