package com.samgye.orderapp.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.samgye.orderapp.R
import com.samgye.orderapp.api.ApiClient
import com.samgye.orderapp.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName
    private lateinit var binding: ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIntroBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        startTimer(isNetworkConnected())
    }

    private fun startTimer (isNetwork: Boolean) {
        if (isNetwork)  {
            Handler(Looper.getMainLooper()).postDelayed({
                if (ApiClient.instance.hasToken()) {
                    ApiClient.instance.userInfo() { result, error ->
                        if (error != null) {
                            // 에러
                            Log.e(TAG, "userInfo failed: ${error.message}")
                            // 팝업 후 finish (에러 코드)
                            finish() // 임시 코드
                        } else if(result != null) {
                            Log.d(TAG, "UserInfoSuccess")
                            if (result.username == null) {
                                Log.d(TAG, "username is empty. go to set username")
                                val userNameIntent = Intent(this, UserNameActivity::class.java)
                                startActivity(userNameIntent)
                                finish()
                            } else {
                                Log.d("UserInfoSuccess", "username is not empty")
                                val homeIntent = Intent(this, HomeActivity::class.java)
                                homeIntent.putExtra("userInfo", result)
                                startActivity(homeIntent)
                                finish()
                            }
                        }
                    }
                } else {
                    Log.d(TAG, "user has not token")
                    val homeIntent = Intent(this, HomeActivity::class.java)
                    homeIntent.putExtra("userInfo", "")
                    startActivity(homeIntent)
                    finish()
                }
            }, 2000)
        } else {
            // 팝업 띄우고 확인 시 finish (네트워크)
            finish() // 임시 코드
        }
    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}