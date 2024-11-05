package com.samgye.orderapp.api

import com.samgye.orderapp.api.response.UserInfoResponse
import com.samgye.orderapp.utils.Constants
import retrofit2.Call
import retrofit2.http.GET

interface ApiBearerService {
    // Login
    @GET(Constants.USER_INFO_PATH)
    fun getUserInfo(): Call<UserInfoResponse>
}