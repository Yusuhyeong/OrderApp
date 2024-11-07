package com.samgye.orderapp.activity.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.samgye.orderapp.Samgye
import com.samgye.orderapp.api.ApiClient

class LoginViewModel : ViewModel() {
    private val TAG = this::class.java.simpleName
    private val _user_accesstoken = MutableLiveData<String>()
    val user_accesstoken: LiveData<String>
        get() = _user_accesstoken
    fun loginCheck() {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(Samgye.applicationContext)) { // 카카오톡 app 설치 유무 확인
            UserApiClient.instance.loginWithKakaoTalk(Samgye.applicationContext) { token, error ->
                if (error != null) {
                    Log.d(TAG, "카카오톡으로 로그인 실패! " + error.message)

                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    UserApiClient.instance.loginWithKakaoAccount(
                        Samgye.applicationContext,
                        callback = callback
                    )

                } else if (token != null) {
                    Log.d(TAG, "카카오톡으로 로그인 성공! " + token.accessToken)
                    checkUserName()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(
                Samgye.applicationContext,
                callback = callback
            )
        }
    }
    private fun checkUserName() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패: ${error.message}")
            } else if (user != null) {
                Log.d(TAG, "사용자 정보 요청 성공: ${user.kakaoAccount?.profile?.nickname}")
                Log.d(TAG, "사용자 ID: ${user.id}")

                val userId = user.id.toString()
                Log.d(TAG, "uid : $userId")

                ApiClient.instance.loginKakao(userId, "kakao") { result, error ->
                    if (error != null) {
                        // 에러 처리
                        Log.e(TAG, "Login failed: ${error.message}")
                        _user_accesstoken.value = "-1"
                    } else if (result != null) {
                        // 성공적으로 로그인한 경우
                        Log.d(TAG, "api success")
                        Log.d(TAG, "Token Type: ${result.tokenType}")
                        Log.d(TAG, "Access Token: ${result.accessToken}")
                        Log.d(TAG, "Username: ${result.username}")

                        _user_accesstoken.value = result.accessToken
                    }
                }
            }
        }
    }

    private val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(TAG, "카카오 계정으로 로그인 실패! " + error.message)
            Log.d(TAG, "로그인 실패")
        } else if (token != null) {
            Log.d(TAG, "카카오 계정으로 로그인 성공! " + token.accessToken)
            Log.d(TAG, "로그인 성공")
            checkUserName()
        }
    }
}