package com.samgye.orderapp.api

import android.util.Log
import com.samgye.orderapp.api.network.ApiFactory
import com.samgye.orderapp.api.request.LoginRequest
import com.samgye.orderapp.api.response.LoginResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiClient (
    private val apiService: ApiService = ApiFactory.itsApi.create(ApiService::class.java)
) {
    fun loginKakao(loginRequest: LoginRequest, callback: (LoginResult?, error: Throwable?) -> Unit) {
        apiService.loginKakao(loginRequest).enqueue(object : Callback<LoginResult> {
            override fun onResponse(call: Call<LoginResult>, response: Response<LoginResult>) {
                if (response.isSuccessful) {
                    Log.d("TEST", "success")
                    callback(response.body(), null)
                } else {
                    Log.d("TEST", "fail")
                    callback(null, Throwable(response.errorBody()?.string()))
                }
            }

            override fun onFailure(call: Call<LoginResult>, t: Throwable) {
                callback(null, t) // 네트워크 실패 시 에러 반환
            }
        })
    }

    companion object {
        /**
         * 간편한 API 호출을 위해 기본 제공되는 singleton 객체
         */
        @JvmStatic
        val instance by lazy { ApiClient() }
    }
}
