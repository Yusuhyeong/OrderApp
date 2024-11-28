package com.samgye.orderapp.api

import android.content.SharedPreferences
import android.util.Log
import com.samgye.orderapp.Samgye
import com.samgye.orderapp.api.network.ItsJson
import com.samgye.orderapp.api.response.TokenResponse
import com.samgye.orderapp.utils.PersistentKVStore
import com.samgye.orderapp.utils.SharedPrefsWrapper

/**
 * 토큰 저장소 구현체.
 *
 * 기기 고유값을 이용해 토큰을 암호화하고 [SharedPreferences]에 저장함.
 *
 *  ```kotlin
 *  // 저장된 토큰 가져오기
 *  val token = TokenManager.instance.getToken()
 *  ```
 *
 * @see TokenManageable
 * @see TokenManagerProvider
 */
class TokenManager(
    /** @suppress */
    val appCache: PersistentKVStore = SharedPrefsWrapper(Samgye.mSharedPreferences)
) : TokenManageable {
    private val TAG = this.javaClass.name
    private var currentToken: TokenResponse?

    init {
        currentToken = appCache.getString(tokenKey)?.let {
            try {
                ItsJson.fromJson<TokenResponse>(
                    it,
                    TokenResponse::class.java
                )
            } catch (e: Throwable) {
                Log.e(TAG, "", e)
                null
            }
        }
    }

    /**
     * [SharedPreferences]에 저장되어 있는 [TokenResponse] 반환.
     */
    override fun getToken(): TokenResponse? {
        return currentToken
    }

    /**
     * 토큰을 [SharedPreferences]에 저장.
     *
     * @param token 저장하고자 하는 [TokenResponse] 객체.
     */
    @Synchronized
    override fun setToken(token: TokenResponse) {
        val newToken = TokenResponse(
            tokenType = token.tokenType,
            accessToken = token.accessToken,
            refreshToken = token.refreshToken,
            username = token.username,
            expiresIn = token.expiresIn
        )
        try {
            appCache.putString(tokenKey, ItsJson.toJson(newToken)).commit()
        } catch (e: Throwable) {
            Log.e(TAG, "", e)
        }
        currentToken = newToken
    }

    /**
     * [SharedPreferences]에 저장되어 있는 [TokenResponse] 객체를 삭제.
     */
    override fun clear() {
        currentToken = null
        appCache.remove(tokenKey).commit()
        appCache.remove(menuList).commit()
    }

    companion object {

        /**
         * 토큰 저장소 singleton 객체
         */
        @JvmStatic
        val instance by lazy { TokenManager() }

        /** @suppress */
        const val tokenKey = "com.samgye.orderapp.oauth_token"
        const val menuList = "menuList"
    }
}

private inline fun <T> parseOrNull(f: () -> T): T? =
    try {
        f()
    } catch (e: Exception) {
        Log.e("TokenManager", "", e)
        null
    }