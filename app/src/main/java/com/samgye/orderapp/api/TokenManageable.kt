package com.samgye.orderapp.api

import com.samgye.orderapp.api.response.TokenResponse

/**
 * API에 사용되는 액세스 토큰, 리프레시 토큰을 관리하는 프로토콜.
 */
interface TokenManageable {
    /**
     * 저장되어 있는 [TokenResponse] 반환.
     */
    fun getToken(): TokenResponse?

    /**
     * 토큰을 저장.
     *
     * @param token 저장하고자 하는 [TokenResponse] 객체.
     */
    fun setToken(token: TokenResponse)

    /**
     * 저장되어 있는 [TokenResponse] 객체를 삭제.
     */
    fun clear()
}