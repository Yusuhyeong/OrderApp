package com.samgye.orderapp

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.samgye.orderapp.activity.viewmodel.UserInfoViewModel

class MyApp: Application() {
    lateinit var userInfoViewModel: UserInfoViewModel
    override fun onCreate() {
        super.onCreate()

        userInfoViewModel = ViewModelProvider(
            ViewModelStore(),
            ViewModelProvider.AndroidViewModelFactory(this)
        )[UserInfoViewModel::class.java]

        KakaoSdk.init(this.applicationContext, "1625c91b92367b550db25405ba1d235e")
        val keyHash = Utility.getKeyHash(this)
        Log.d("TEST", keyHash)

        Samgye.applicationContext = this
        Samgye.mSharedPreferences = this.getSharedPreferences("samgyeOrderApp", Context.MODE_PRIVATE)
        Samgye.userInfoViewModel = this.userInfoViewModel
    }
}