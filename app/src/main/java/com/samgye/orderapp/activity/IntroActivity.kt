package com.samgye.orderapp.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.samgye.orderapp.Samgye
import com.samgye.orderapp.activity.viewmodel.LoginViewModel
import com.samgye.orderapp.activity.viewmodel.UserInfoViewModel
import com.samgye.orderapp.api.ApiClient
import com.samgye.orderapp.databinding.ActivityIntroBinding
import com.samgye.orderapp.fragment.AlertFragment

class IntroActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName
    private lateinit var binding: ActivityIntroBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var userInfoViewModel: UserInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIntroBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        userInfoViewModel = Samgye.userInfoViewModel

        if (isNetworkConnected()) {
            if (ApiClient.instance.hasToken()) {
                loginViewModel.refreshToken()
            } else {
                Log.d(TAG, "HomeActivity로 이동. no user")
                val homeIntent = Intent(this, HomeActivity::class.java)
                startActivity(homeIntent)
                finish()
            }
        }

        loginViewModel.is_token_refresh.observe(this) { refresh ->
            if (refresh) {
                userInfoViewModel.loadUserInfo()
            } else {
                // error
                if (!ApiClient.instance.hasToken()) {
                    AlertFragment()
                        .setTitle("로그인 실패")
                        .setMessage("사용자 정보를 조회하는데 실패하였습니다.")
                        .setIsOneBtn(true)
                        .setPositiveButton {
                            finish()
                        }
                        .show(supportFragmentManager, "AlertFragment")
                }
                ApiClient.instance.logout()
                finish()
            }
        }

        userInfoViewModel.is_username_null.observe(this) { isNull ->
            if (isNull) {
                Handler().postDelayed(Runnable {
                    val usernameIntent = Intent(this, UserNameActivity::class.java)
                    startActivity(usernameIntent)
                    finish()
                }, 3000)
            }
        }

        userInfoViewModel.user_info.observe(this) { userInfo ->
            userInfo?.let {
                Log.d(TAG, "HomeActivity로 이동")
                Handler().postDelayed(Runnable {
                    val homeIntent = Intent(this, HomeActivity::class.java)
                    startActivity(homeIntent)
                    finish()
                }, 3000)
            } ?: run {
                // error
                if (!ApiClient.instance.hasToken()) {
                    AlertFragment()
                        .setTitle("로그인 실패")
                        .setMessage("사용자 정보를 조회하는데 실패하였습니다.")
                        .setIsOneBtn(true)
                        .setPositiveButton {
                            finish()
                        }
                        .show(supportFragmentManager, "AlertFragment")
                }
            }
        }
    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}