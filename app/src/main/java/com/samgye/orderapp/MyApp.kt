package com.samgye.orderapp

import android.app.Application
import android.content.Context
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this.applicationContext, "1625c91b92367b550db25405ba1d235e")
        val keyHash = Utility.getKeyHash(this)
        Log.d("TEST", keyHash)

        Samgye.applicationContext = this
        Samgye.mSharedPreferences = this.getSharedPreferences("samgyeOrderApp", Context.MODE_PRIVATE)
    }
}