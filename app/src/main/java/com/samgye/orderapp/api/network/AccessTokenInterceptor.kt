package com.samgye.orderapp.api.network

//import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.samgye.orderapp.api.AuthApiManager
import com.samgye.orderapp.api.TokenManagerProvider
import com.samgye.orderapp.utils.Constants
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * @suppress
 *
 * API 요청에 AccessToken을 추가하는 인터셉터
 * -401 발생시 자동 갱신
 */
class AccessTokenInterceptor(
    private val tokenManagerProvider: TokenManagerProvider = TokenManagerProvider.instance,
    private val manager: AuthApiManager = AuthApiManager.instance
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val usedAccessToken = tokenManagerProvider.manager.getToken()?.accessToken

        val request =
            usedAccessToken?.let {
                chain.request().withAccessToken(it)
            } ?: chain.request()

        val response = chain.proceed(request)

        // -401 발생시 자동 갱신
        if (response.code == 401) {

            // 나중에 들어온 요청들 pending (중복 갱신 방어)
            synchronized(this) {

                // resume 돼서 들어왔을 때 현재 토큰 보고
                val currentToken = tokenManagerProvider.manager.getToken()
                if (currentToken != null ) {

                    val accessToken =
                        if (currentToken.accessToken != usedAccessToken) {
                            // 이전 요청에서 넣었던 토큰과 현재 토큰이 다르면
                            // 이미 앞의 요청에서 갱신됐다고 판단하고, 현재 토큰 사용
                            currentToken.accessToken
                        }
                        else {
                            try {
                                // 갱신 요청 이후 토큰 사용
                                manager.refreshAccessToken(currentToken).accessToken
                            } catch (e: Throwable) {
//                                FirebaseCrashlytics.getInstance().recordException(e)
                                throw e
                            }
                        }

                    // 변경된 accessToken으로 API 재시도
                    return chain.proceed(request.withAccessToken(accessToken))
                }
            }
        }

        return response
    }
}

/**
 * @suppress
 */
inline fun Request.withAccessToken(accessToken: String) =
    newBuilder()
        .removeHeader(Constants.AUTHORIZATION)
        .addHeader(Constants.AUTHORIZATION, "${Constants.BEARER} $accessToken")
        .build()
