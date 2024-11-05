package com.samgye.orderapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.samgye.orderapp.R
import com.samgye.orderapp.api.ApiClient
import com.samgye.orderapp.api.request.LoginRequest

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ApiClient.instance.loginKakao("1234", "kakao") { result, error ->
            if (error != null) {
                // 에러 처리
                Log.e("LoginError", "Login failed: ${error.message}")
            } else if (result != null) {
                // 성공적으로 로그인한 경우
                Log.d("LoginSuccess", "api success")
                Log.d("LoginSuccess", "Token Type: ${result.tokenType}")
                Log.d("LoginSuccess", "Access Token: ${result.accessToken}")
                Log.d("LoginSuccess", "Username: ${result.username}")
            }
        }
    }
}