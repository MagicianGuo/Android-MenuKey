package com.magicianguo.menukey.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.magicianguo.menukey.R
import com.magicianguo.menukey.util.MenuKeyViewTools
import com.magicianguo.menukey.util.SPUtils

class KeyOrderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    private val mOrderBack: OrderItemView
    private val mOrderHome: OrderItemView
    private val mOrderRecent: OrderItemView
    private val mOrderMenu: OrderItemView
    private val mLLOrder: View
    private val mBtnSave: Button
    private val mBtnCancel: Button
    private val mBtnChange: Button
    private val mClickItemListener: OrderItemView.IClickItemListener
    private var mOrderItems: Array<OrderItemView>? = null
    private val mOrderList = mutableListOf<OrderItemView>()
    init {
        LayoutInflater.from(context).inflate(R.layout.layout_key_order_view, this)
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        mOrderBack = findViewById(R.id.order_back)
        mOrderHome = findViewById(R.id.order_home)
        mOrderRecent = findViewById(R.id.order_recent)
        mOrderMenu = findViewById(R.id.order_menu)
        mLLOrder = findViewById(R.id.ll_order)
        mBtnSave = findViewById(R.id.btn_save)
        mBtnCancel = findViewById(R.id.btn_cancel)
        mBtnChange = findViewById(R.id.btn_change)
        mClickItemListener = object : OrderItemView.IClickItemListener {
            override fun onClickItem(v: OrderItemView, isCheck: Boolean) {
                resolveClickItemEvent(v, isCheck)
            }
        }
        mOrderBack.setClickItemListener(mClickItemListener)
        mOrderHome.setClickItemListener(mClickItemListener)
        mOrderRecent.setClickItemListener(mClickItemListener)
        mOrderMenu.setClickItemListener(mClickItemListener)
        mBtnChange.setOnClickListener {
            showEdit(true)
        }
        mBtnSave.setOnClickListener {
            val orderFormat = (mOrderList[0].getOrderType() shl 12) +
                    (mOrderList[1].getOrderType() shl 8) +
                    (mOrderList[2].getOrderType() shl 4) +
                    mOrderList[3].getOrderType()
            MenuKeyViewTools.setOrder(orderFormat)
            SPUtils.setOrder(orderFormat)
            showEdit(false)
        }
        mBtnCancel.setOnClickListener {
            showEdit(false)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mOrderItems = arrayOf(mOrderBack, mOrderHome, mOrderRecent, mOrderMenu)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mOrderItems = null
    }

    private fun showEdit(b: Boolean) {
        mBtnChange.isVisible = !b
        mLLOrder.isVisible = b
        mBtnSave.isVisible = b
        mBtnCancel.isVisible = b
        if (b) {
            mOrderList.clear()
            updateViewState()
        }
    }

    private fun resolveClickItemEvent(v: OrderItemView, isCheck: Boolean) {
        if (isCheck) {
            mOrderList.add(v)
        } else {
            mOrderList.remove(v)
        }
        updateViewState()
    }

    private fun updateViewState() {
        mOrderItems?.forEach {
            it.setOrderNum(-1)
        }
        mOrderList.forEachIndexed { index, orderItemView ->
            orderItemView.setOrderNum(index + 1)
        }
        mBtnSave.isEnabled = mOrderList.size == 4
    }
}