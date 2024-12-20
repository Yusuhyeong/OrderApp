package com.samgye.orderapp.api

import com.samgye.orderapp.api.request.OrderRequest
import com.samgye.orderapp.api.request.UsernameRequest
import com.samgye.orderapp.api.response.BaseResponse
import com.samgye.orderapp.api.response.MenuDataResponse
import com.samgye.orderapp.api.response.MenuListResponse
import com.samgye.orderapp.api.response.UserInfoResponse
import com.samgye.orderapp.api.response.UserDetailResponse
import com.samgye.orderapp.utils.Constants
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiBearerService {
    @GET(Constants.USER_INFO_PATH)
    fun getUserInfo(): Call<BaseResponse<UserInfoResponse>>
    @GET(Constants.USER_POINT_INFO_PATH)
    fun getUserDetail(): Call<BaseResponse<UserDetailResponse>>
    @POST(Constants.USERNAME_UPDATE_PATH)
    fun updateUsername(@Body usernameRequest: UsernameRequest): Call <BaseResponse<Int>>
    @GET(Constants.MENU_INFO_PATH)
    fun getMenuInfo(): Call<BaseResponse<List<MenuDataResponse<List<MenuListResponse>>>>>
    @POST(Constants.MENU_ORDER_PATH)
    fun orderMenu(@Body orderRequest: OrderRequest) : Call <BaseResponse<Int>>
}