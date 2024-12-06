package com.samgye.orderapp

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.samgye.orderapp.activity.viewmodel.PopupViewModel
import com.samgye.orderapp.activity.viewmodel.UserInfoViewModel
import com.samgye.orderapp.data.PopupData
import com.samgye.orderapp.fragment.CommonPopupFragment

class MyApp: Application() {
    private lateinit var userInfoViewModel: UserInfoViewModel
    private lateinit var popupViewModel: PopupViewModel

    override fun onCreate() {
        super.onCreate()

        userInfoViewModel = ViewModelProvider(
            ViewModelStore(),
            ViewModelProvider.AndroidViewModelFactory(this)
        )[UserInfoViewModel::class.java]

        popupViewModel = ViewModelProvider(
            ViewModelStore(),
            ViewModelProvider.AndroidViewModelFactory(this)
        )[PopupViewModel::class.java]

        KakaoSdk.init(this.applicationContext, "1625c91b92367b550db25405ba1d235e")
        val keyHash = Utility.getKeyHash(this)
        Log.d("TEST", keyHash)

        Samgye.applicationContext = this
        Samgye.mSharedPreferences = this.getSharedPreferences("samgyeOrderApp", Context.MODE_PRIVATE)
        Samgye.userInfoViewModel = this.userInfoViewModel
        Samgye.popupViewModel = this.popupViewModel
    }
}