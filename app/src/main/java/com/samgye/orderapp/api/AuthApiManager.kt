package com.samgye.orderapp.api

import com.samgye.orderapp.api.network.ApiFactory
import com.samgye.orderapp.api.request.LoginRequest
import com.samgye.orderapp.api.request.RefreshTokenRequest
import com.samgye.orderapp.api.response.TokenResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @suppress
 */

class AuthApiManager (
    private val authApi: ApiAuthService = ApiFactory.apiAuth.create(ApiAuthService::class.java),
    /** @suppress */ val tokenManagerProvider: TokenManagerProvider = TokenManagerProvider.instance
) {

    /**
     * @suppress
     */
    internal fun hasToken(): Boolean {
        return tokenManagerProvider.manager.getToken() != null
    }

    /**
     * @suppress
     */
    internal fun login(
        body: LoginRequest,
        callback: (token: TokenResponse?, error: Throwable?) -> Unit
    ) {
        authApi.login(body)
            .enqueue(object : Callback<TokenResponse> {
            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                callback(null, t)
            }

            override fun onResponse(
                call: Call<TokenResponse>,
                response: Response<TokenResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { token ->
                        tokenManagerProvider.manager.setToken(token)
                        callback(token, null)
                        return
                    }
                    callback(null, Throwable("응답오류. No Body"))
                } else {
                    callback(null, Throwable(response.errorBody().toString()))
                }
            }
        })
    }

    /**
     * @suppress
     */
    @JvmOverloads
    internal fun refreshAccessToken(
        oldToken: TokenResponse = tokenManagerProvider.manager.getToken() ?: throw Error("RefreshToken을 찾을 수 없습니다. 로그인해주세요."),
        callback: (token: TokenResponse?, error: Throwable?) -> Unit
    ) {
        val oldToken = RefreshTokenRequest(oldToken.refreshToken ?: throw java.lang.Exception())
        authApi.refreshToken(oldToken).enqueue(object : Callback<TokenResponse> {

            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                val model = response.body()?.let {
                    tokenManagerProvider.manager.setToken(it)
                    callback(it, null)
                } ?: kotlin.run {
                    callback(null, Throwable("응답오류. RefreshToken을 찾을 수 없습니다."))
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    /**
     * @suppress
     */
    internal fun refreshAccessToken(
        oldToken: TokenResponse = tokenManagerProvider.manager.getToken() ?: throw Error("RefreshToken을 찾을 수 없습니다. 먼저 로그인해주세요.")
    ): TokenResponse {
        val refreshTokenRequest = RefreshTokenRequest(oldToken.refreshToken ?: throw java.lang.Exception())
        val response =
            authApi.refreshToken(refreshTokenRequest).execute()

        val token = response.body()?.let { token ->
            token
        } ?: throw Exception("RefreshToken 갱신에 실패하였습니다.")

        tokenManagerProvider.manager.setToken(token)
        return token
    }

    internal fun logout() {
        tokenManagerProvider.manager.clear()
    }

    companion object {
        @JvmStatic
        val instance by lazy { AuthApiManager() }
    }
}