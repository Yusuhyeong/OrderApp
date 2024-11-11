package com.samgye.orderapp.api

import com.samgye.orderapp.api.request.UsernameRequest
import com.samgye.orderapp.api.response.BaseResponse
import com.samgye.orderapp.api.response.UserInfoResponse
import com.samgye.orderapp.api.response.UserPointResponse
import com.samgye.orderapp.utils.Constants
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiBearerService {
    // Login
    @GET(Constants.USER_INFO_PATH)
    fun getUserInfo(): Call<BaseResponse<UserInfoResponse>>
    @GET(Constants.USER_POINT_INFO_PATH)
    fun getUserPoint(): Call<BaseResponse<UserPointResponse>>
    @POST(Constants.USERNAME_UPDATE_PATH)
    fun updateUsername(@Body usernameRequest: UsernameRequest): Call <BaseResponse<Int>>
}