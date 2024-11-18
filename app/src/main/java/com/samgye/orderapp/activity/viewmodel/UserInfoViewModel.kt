package com.samgye.orderapp.activity.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.samgye.orderapp.api.ApiClient
import com.samgye.orderapp.data.UserInfo

class UserInfoViewModel(application: Application) : AndroidViewModel(application) {
    private val _user_info = MutableLiveData<UserInfo>()
    val user_info: LiveData<UserInfo>
        get() = _user_info

    private val _is_username_null = MutableLiveData<Boolean>()
    val is_username_null: LiveData<Boolean>
        get() = _is_username_null

    fun loadUserInfo() {
        ApiClient.instance.userInfo() { result, error ->
            var username: String
            var snsType: String
            var userPoint: Int
            val loginStatus = ApiClient.instance.hasToken()

            if (error != null) {
                // error
            } else {
                if (result?.username == null) {
                    _is_username_null.value = true
                } else {
                    username = result.username
                    snsType = result.snsType

                    ApiClient.instance.userDetailInfo() { point, error ->
                        if (error != null) {
                            // error
                        } else {
                            if (point?.point == null) {
                                // error
                            } else {
                                userPoint = point.point

                                Log.d("UserInfoViewModel", "username : $username, snsType : $snsType, point : $userPoint, loginStatus: $loginStatus")
                                val userInfo = UserInfo(username, snsType, userPoint, loginStatus)

                                _user_info.value = userInfo
                            }
                        }
                    }
                }
            }
        }
    }

    fun clearUserInfo() {
        _user_info.value = null
    }
}