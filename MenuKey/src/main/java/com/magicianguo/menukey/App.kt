package com.magicianguo.menukey

import android.app.Application

class App : Application() {
    companion object {
        private lateinit var instance: App
        fun get() = instance
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}