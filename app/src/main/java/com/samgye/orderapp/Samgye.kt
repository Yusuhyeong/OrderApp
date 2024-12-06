package com.samgye.orderapp

import android.content.Context
import android.content.SharedPreferences
import com.samgye.orderapp.activity.viewmodel.PopupViewModel
import com.samgye.orderapp.activity.viewmodel.UserInfoViewModel

object Samgye {
    lateinit var applicationContext: Context
    lateinit var mSharedPreferences: SharedPreferences
    lateinit var userInfoViewModel: UserInfoViewModel
    lateinit var popupViewModel: PopupViewModel
}