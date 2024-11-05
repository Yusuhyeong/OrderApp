package com.samgye.orderapp.api

import android.util.Log
import com.samgye.orderapp.api.network.ApiFactory
import com.samgye.orderapp.api.request.LoginRequest
import com.samgye.orderapp.api.response.TokenResponse
import com.samgye.orderapp.api.response.UserInfoResponse
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
        apiBearer.getUserInfo().enqueue(object : Callback<UserInfoResponse> {
            override fun onResponse(
                call: Call<UserInfoResponse>,
                response: Response<UserInfoResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { info ->
                        Log.d("UserInfoResponse", "snsType: ${info.snsType}")
                        Log.d("UserInfoResponse", "username: ${info.username}")
                        return
                    }
                    callback(null, Throwable("응답오류. No Body"))
                } else {
                    callback(null, Throwable(response.errorBody().toString()))
                }
            }
            override fun onFailure(
                call: Call<UserInfoResponse>,
                t: Throwable) {
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
