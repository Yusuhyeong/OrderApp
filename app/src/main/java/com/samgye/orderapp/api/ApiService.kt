package com.samgye.orderapp.api

import com.samgye.orderapp.api.request.LoginRequest
import com.samgye.orderapp.api.response.LoginResult
import com.samgye.orderapp.utils.Constants
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    // Login
    @POST(Constants.LOGIN_PATH)
    fun loginKakao(@Body loginRequest: LoginRequest): Call<LoginResult>
}