package com.samgye.orderapp.api

import android.util.Log
import com.samgye.orderapp.api.network.ApiFactory
import com.samgye.orderapp.api.request.LoginRequest
import com.samgye.orderapp.api.request.UsernameRequest
import com.samgye.orderapp.api.response.BaseResponse
import com.samgye.orderapp.api.response.TokenResponse
import com.samgye.orderapp.api.response.UserInfoResponse
import com.samgye.orderapp.api.response.UsernameResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiClient (
    private val apiBearer: ApiBearerService = ApiFactory.apiBearer.create(ApiBearerService::class.java),
    private val manager: AuthApiManager = AuthApiManager.instance,
    private val tokenManagerProvider: TokenManagerProvider = TokenManagerProvider.instance
) {
    fun loginKakao(
        id: String,
        type: String = "kakao",
        callback: (token: TokenResponse?, error: Throwable?) -> Unit
    ) = manager.login(LoginRequest(snsId = id, snsType = type), callback)

    fun hasToken(): Boolean {
        Log.d("ApiClient", "hasToken : ${manager.hasToken()}")
        return manager.hasToken()
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

    fun updateUserName(usernameRequest: UsernameRequest, callback: (data: UsernameResponse?, error: Throwable?) -> Unit) {
        apiBearer.updateUsername(usernameRequest).enqueue(object : Callback<BaseResponse<UsernameResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<UsernameResponse>>,
                response: Response<BaseResponse<UsernameResponse>>
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

            override fun onFailure(call: Call<BaseResponse<UsernameResponse>>, t: Throwable) {
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
