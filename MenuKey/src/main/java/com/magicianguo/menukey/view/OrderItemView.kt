package com.magicianguo.menukey.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.magicianguo.menukey.R

class OrderItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
    private val mIvKeyType: ImageView
    private val mTvOrderNum: TextView
    private var mClickListener: IClickItemListener? = null
    private var mIsCheck = false
    private var mOrderType = 1

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_order_item_view, this)
        mIvKeyType = findViewById(R.id.iv_key_type)
        mTvOrderNum = findViewById(R.id.tv_order_num)
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.OrderItemView)
        val resId = attributes.getResourceId(R.styleable.OrderItemView_img_src, R.mipmap.ic_back)
        mOrderType = attributes.getInteger(R.styleable.OrderItemView_order_type, 1)
        mIvKeyType.setImageResource(resId)
        attributes.recycle()
        setOnClickListener {
            mIsCheck = !mIsCheck
            mClickListener?.onClickItem(this, mIsCheck)
        }
    }

    fun setClickItemListener(listener: IClickItemListener) {
        mClickListener = listener
    }

    fun setOrderNum(orderNum: Int) {
        if (orderNum == -1) {
            mIsCheck = false
            mTvOrderNum.isVisible = false
        } else {
            mIsCheck = true
            mTvOrderNum.isVisible = true
            mTvOrderNum.text = "$orderNum"
        }
    }

    fun getOrderType(): Int {
        return mOrderType
    }

    interface IClickItemListener {
        fun onClickItem(v: OrderItemView, isCheck: Boolean)
    }
}