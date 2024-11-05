package com.samgye.orderapp.api

import com.samgye.orderapp.api.network.ApiFactory
import com.samgye.orderapp.api.request.LoginRequest
import com.samgye.orderapp.api.response.TokenResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiClient (
    private val apiAuthService: ApiAuthService = ApiFactory.apiAuth.create(ApiAuthService::class.java),
    private val apiBearer: ApiBearerService = ApiFactory.apiBearer.create(ApiBearerService::class.java),
    private val manager: AuthApiManager = AuthApiManager.instance,
    private val tokenManagerProvider: TokenManagerProvider = TokenManagerProvider.instance
) {
    fun loginKakao(loginRequest: LoginRequest, callback: (TokenResponse?, error: Throwable?) -> Unit) {
        apiAuthService.login(loginRequest).enqueue(object : Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable(response.errorBody()?.string()))
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                callback(null, t) // 네트워크 실패 시 에러 반환
            }
        })
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

    companion object {
        /**
         * 간편한 API 호출을 위해 기본 제공되는 singleton 객체
         */
        @JvmStatic
        val instance by lazy { ApiClient() }
    }
}
