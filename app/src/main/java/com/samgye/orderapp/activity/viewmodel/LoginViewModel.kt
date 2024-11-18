package com.samgye.orderapp.activity.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samgye.orderapp.api.ApiClient

class LoginViewModel : ViewModel() {
    private val _is_token_refresh = MutableLiveData<Boolean>()
    val is_token_refresh: LiveData<Boolean>
        get() = _is_token_refresh

    private val _is_login_end = MutableLiveData<Boolean>()
    val is_login_end: LiveData<Boolean>
        get() = _is_login_end

    fun refreshToken() {
        ApiClient.instance.refreshAccessToken { token, error ->
            _is_token_refresh.value = (error == null)
        }
    }

    fun login(userId: String, type: String) {
        ApiClient.instance.login(userId, type) { result, error ->
            if (error != null) {
                _is_login_end.value = false
            } else {
                _is_login_end.value = result != null
            }
        }
    }
}