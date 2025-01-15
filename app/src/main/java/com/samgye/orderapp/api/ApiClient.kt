package com.samgye.orderapp.api

import android.util.Log
import com.samgye.orderapp.api.network.ApiFactory
import com.samgye.orderapp.api.request.LoginRequest
import com.samgye.orderapp.api.request.NoticeDetailRequest
import com.samgye.orderapp.api.request.OrderRequest
import com.samgye.orderapp.api.request.UsernameRequest
import com.samgye.orderapp.api.response.BaseResponse
import com.samgye.orderapp.api.response.NoticeDetailResponse
import com.samgye.orderapp.api.response.NoticeInfoResponse
import com.samgye.orderapp.api.response.MenuDataResponse
import com.samgye.orderapp.api.response.MenuListResponse
import com.samgye.orderapp.api.response.OrderListResponse
import com.samgye.orderapp.api.response.OrderMenuResponse
import com.samgye.orderapp.api.response.TokenResponse
import com.samgye.orderapp.api.response.UserInfoResponse
import com.samgye.orderapp.api.response.UserDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiClient (
    private val apiBearer: ApiBearerService = ApiFactory.apiBearer.create(ApiBearerService::class.java),
    private val manager: AuthApiManager = AuthApiManager.instance,
    private val tokenManagerProvider: TokenManagerProvider = TokenManagerProvider.instance,
    private val apiBasic: ApiBasicService = ApiFactory.apiBasic.create(ApiBasicService::class.java)
) {
    fun login(
        id: String,
        type: String,
        callback: (token: TokenResponse?, error: Throwable?) -> Unit
    ) = manager.login(LoginRequest(snsId = id, snsType = type), callback)

    fun hasToken(): Boolean {
        Log.d("ApiClient", "hasToken : ${manager.hasToken()}")
        return manager.hasToken()
    }

    fun logout() {
        Log.d("ApiClient", "${manager.hasToken()} logout")
        manager.logout()
    }

    /**
     * 기존 토큰을 갱신합니다
     *
     * @param oldToken 기존 토큰
     * @param callback 발급 받은 [TokenResponse] 반환.
     */
    @JvmOverloads
    fun refreshAccessToken(
        oldToken: TokenResponse = tokenManagerProvider.manager.getToken() ?: throw Error("RefreshToken을 찾을 수 없습니다. 로그인해주세요."),
        callback: (token: TokenResponse?, error: Throwable?) -> Unit
    ) = manager.refreshAccessToken(oldToken, callback)


    /**
     * 사용자 정보를 조회합니다
     *
     * @param callback 보유 token에 대한 [UserInfoResponse] 반환.
     */
    fun userInfo(callback: (info: UserInfoResponse?, error: Throwable?) -> Unit) {
        apiBearer.getUserInfo().enqueue(object : Callback<BaseResponse<UserInfoResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<UserInfoResponse>>,
                response: Response<BaseResponse<UserInfoResponse>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { info ->
                        Log.d("UserInfoResponse", "snsType: ${info.data?.snsType}")
                        Log.d("UserInfoResponse", "username: ${info.data?.username}")
                        callback(info.data, null)
                        return
                    }
                    callback(null, Throwable("응답오류. No Body"))
                } else {
                    callback(null, Throwable(response.errorBody().toString()))
                }
            }
            override fun onFailure(
                call: Call<BaseResponse<UserInfoResponse>>,
                t: Throwable) {
                callback(null, t)
            }
        })
    }

    fun userDetailInfo(callback: (point: UserDetailResponse?, error: Throwable?) -> Unit) {
        apiBearer.getUserDetail().enqueue(object : Callback<BaseResponse<UserDetailResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<UserDetailResponse>>,
                response: Response<BaseResponse<UserDetailResponse>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { info ->
                        Log.d("PointInfoResponse", "point: ${info.data?.point}")
                        callback(info.data, null)
                        return
                    }
                    callback(null, Throwable("응답오류. No Body"))
                } else {
                    callback(null, Throwable(response.errorBody().toString()))
                }
            }

            override fun onFailure(call: Call<BaseResponse<UserDetailResponse>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    fun updateUserName(usernameRequest: UsernameRequest, callback: (data: Int?, error: Throwable?) -> Unit) {
        apiBearer.updateUsername(usernameRequest).enqueue(object : Callback<BaseResponse<Int>> {
            override fun onResponse(
                call: Call<BaseResponse<Int>>,
                response: Response<BaseResponse<Int>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        callback(data.data, null)
                        return
                    }
                    callback(null, Throwable("응답오류. no Body"))
                } else {
                    callback(null, Throwable(response.errorBody().toString()))
                }
            }

            override fun onFailure(call: Call<BaseResponse<Int>>, t: Throwable) {
                callback(null, t)
            }

        })
    }

    fun getMenuInfo(callback: (data: List<MenuDataResponse<List<MenuListResponse>>>?, error: Throwable?) -> Unit) {
        apiBearer.getMenuInfo().enqueue(object : Callback<BaseResponse<List<MenuDataResponse<List<MenuListResponse>>>>> {
            override fun onResponse(
                call: Call<BaseResponse<List<MenuDataResponse<List<MenuListResponse>>>>>,
                response: Response<BaseResponse<List<MenuDataResponse<List<MenuListResponse>>>>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        callback(data.data, null)
                        return
                    }
                    callback(null, Throwable("응답오류. no Body"))
                } else {
                    callback(null, Throwable(response.errorBody().toString()))
                }
            }

            override fun onFailure(
                call: Call<BaseResponse<List<MenuDataResponse<List<MenuListResponse>>>>>,
                t: Throwable
            ) {
                callback(null, t)
            }

        })
    }

    fun getDetailNotice(noticeSeq: NoticeDetailRequest, callback: (notice: BaseResponse<NoticeDetailResponse>?, error: Throwable?) -> Unit) {
        apiBasic.getNoticeDetail(noticeSeq).enqueue(object : Callback<BaseResponse<NoticeDetailResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<NoticeDetailResponse>>,
                response: Response<BaseResponse<NoticeDetailResponse>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { notice ->
                        callback(notice, null)
                        return
                    }
                    callback(null, Throwable("응답오류. no Body"))
                } else {
                    callback(null, Throwable(response.errorBody().toString()))
                }
            }

            override fun onFailure(call: Call<BaseResponse<NoticeDetailResponse>>, t: Throwable) {
                callback(null, t)
            }

        })
    }

    fun getLatestNotice(callback: (notice: BaseResponse<NoticeInfoResponse>?, error: Throwable?) -> Unit) {
        apiBasic.getLatestNoticeTitle().enqueue(object : Callback<BaseResponse<NoticeInfoResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<NoticeInfoResponse>>,
                response: Response<BaseResponse<NoticeInfoResponse>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { notice ->
                        callback(notice, null)
                        return
                    }
                    callback(null, Throwable("응답오류. no Body"))
                } else {
                    callback(null, Throwable(response.errorBody().toString()))
                }
            }

            override fun onFailure(call: Call<BaseResponse<NoticeInfoResponse>>, t: Throwable) {
                callback(null, t)
            }

        })
    }

    fun getAllNotice(callback: (notice: BaseResponse<List<NoticeInfoResponse>>?, error: Throwable?) -> Unit) {
        apiBasic.getAllNoticeTitle().enqueue(object : Callback<BaseResponse<List<NoticeInfoResponse>>> {
            override fun onResponse(
                call: Call<BaseResponse<List<NoticeInfoResponse>>>,
                response: Response<BaseResponse<List<NoticeInfoResponse>>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { notice ->
                        callback(notice, null)
                        return
                    }
                    callback(null, Throwable("응답오류. no Body"))
                } else {
                    callback(null, Throwable(response.errorBody().toString()))
                }
            }

            override fun onFailure(
                call: Call<BaseResponse<List<NoticeInfoResponse>>>,
                t: Throwable
            ) {
                callback(null, t)
            }

        })
    }

    fun orderMenu(orderRequest: OrderRequest, callback: (code: Int?, error: Throwable?) -> Unit) { // code : 2000 -> success, 4000 -> fail
        apiBearer.orderMenu(orderRequest).enqueue(object : Callback<BaseResponse<Int>> {
            override fun onResponse(
                call: Call<BaseResponse<Int>>,
                response: Response<BaseResponse<Int>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { response ->
                        callback(response.code, null)
                        return
                    }
                    callback(null, Throwable("응답오류. no Body"))
                } else {
                    callback(null, Throwable(response.errorBody().toString()))
                }
            }

            override fun onFailure(
                call: Call<BaseResponse<Int>>,
                t: Throwable
            ) {
                callback(null, t)
            }

        })
    }
    fun getOrderList(callback: (orderData: List<OrderListResponse<List<OrderMenuResponse>>>?, error: Throwable?) -> Unit) {
        apiBearer.getOrderList().enqueue(object : Callback<BaseResponse<List<OrderListResponse<List<OrderMenuResponse>>>>> {
            override fun onResponse(
                call: Call<BaseResponse<List<OrderListResponse<List<OrderMenuResponse>>>>>,
                response: Response<BaseResponse<List<OrderListResponse<List<OrderMenuResponse>>>>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { orderData ->
                        callback(orderData.data, null)
                        return
                    }
                    callback(null, Throwable("응답오류. no Body"))
                } else {
                    callback(null, Throwable(response.errorBody().toString()))
                }
            }

            override fun onFailure(
                call: Call<BaseResponse<List<OrderListResponse<List<OrderMenuResponse>>>>>,
                t: Throwable
            ) {
                callback(null, t)
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
