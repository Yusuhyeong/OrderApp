package com.samgye.orderapp.api

import com.samgye.orderapp.api.request.LoginRequest
import com.samgye.orderapp.api.request.RefreshTokenRequest
import com.samgye.orderapp.api.response.TokenResponse
import com.samgye.orderapp.utils.Constants
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiAuthService {
    // Login
    @POST(Constants.LOGIN_PATH)
    fun login(@Body loginRequest: LoginRequest): Call<TokenResponse>
    @POST(Constants.REFRESH_PATH)
    fun refreshToken(@Body body: RefreshTokenRequest): Call<TokenResponse>
}