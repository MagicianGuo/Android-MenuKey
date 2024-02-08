package com.magicianguo.menukey.constant

annotation class KeyOrder {
    companion object {
        const val MASK_ORDER_1 = 0xF000
        const val MASK_ORDER_2 = 0x0F00
        const val MASK_ORDER_3 = 0x00F0
        const val MASK_ORDER_4 = 0x000F
        const val ORDER_BACK = 0x1
        const val ORDER_HOME = 0x2
        const val ORDER_RECENT = 0x3
        const val ORDER_MENU = 0x4
    }
}
