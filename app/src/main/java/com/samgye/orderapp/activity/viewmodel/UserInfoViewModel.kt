package com.samgye.orderapp.activity.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.samgye.orderapp.api.ApiClient
import com.samgye.orderapp.api.request.UsernameRequest
import com.samgye.orderapp.data.UserInfo

class UserInfoViewModel(application: Application) : AndroidViewModel(application) {
    private val _user_info = MutableLiveData<UserInfo>()
    val user_info: LiveData<UserInfo>
        get() = _user_info

    val is_username_null = SingleLiveEvent<Boolean>()

    private val _username_value = MutableLiveData<String>()
    val username_value: LiveData<String>
        get() = _username_value

    val username_api_state = SingleLiveEvent<String>()

    fun loadUserInfo() {
        ApiClient.instance.userInfo() { result, error ->
            val username: String
            val snsType: String
            var userPoint: Int
            val loginStatus = ApiClient.instance.hasToken()

            if (error != null) {
                _user_info.value = null
            } else {
                if (result != null) {
                    username = result.username
                    if (username.isNullOrEmpty()) {
                        is_username_null.value = true
                        _username_value.value = null
                    } else {
                        snsType = result.snsType

                        ApiClient.instance.userDetailInfo() { point, error ->
                            if (error != null) {
                                // error
                            } else {
                                if (point?.point == null) {
                                    // error
                                } else {
                                    userPoint = point.point
                                    val userInfo = UserInfo(username, snsType, userPoint, loginStatus)

                                    _user_info.value = userInfo
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun clearUserInfo() {
        val userInfo = UserInfo(null, null, null, ApiClient.instance.hasToken())
        _user_info.value = userInfo
    }

    fun setUsernameValue(username: String) {
        _username_value.value = username
    }

    fun setUsername() {
        val username = UsernameRequest(username_value.value.toString())
        ApiClient.instance.updateUserName(username) { data, error ->
            if (error != null) {
                username_api_state.value = "error"
            } else {
                if (data != null) {
                    if (data == 1) {
                        username_api_state.value = data.toString()
                    } else {
                        username_api_state.value = data.toString()
                    }
                }
            }
        }
    }
}