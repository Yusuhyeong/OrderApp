package com.samgye.orderapp

import android.app.Application
import android.content.Context

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()

        Samgye.applicationContext = this
        Samgye.mSharedPreferences = this.getSharedPreferences("samgyeOrderApp", Context.MODE_PRIVATE)
    }
}