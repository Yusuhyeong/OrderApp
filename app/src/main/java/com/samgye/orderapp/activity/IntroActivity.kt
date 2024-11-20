package com.samgye.orderapp.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.samgye.orderapp.MyApp
import com.samgye.orderapp.activity.viewmodel.LoginViewModel
import com.samgye.orderapp.activity.viewmodel.PopupViewModel
import com.samgye.orderapp.activity.viewmodel.UserInfoViewModel
import com.samgye.orderapp.api.ApiClient
import com.samgye.orderapp.data.PopupData
import com.samgye.orderapp.databinding.ActivityIntroBinding
import com.samgye.orderapp.fragment.CommonPopupFragment

class IntroActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName
    private lateinit var binding: ActivityIntroBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var userInfoViewModel: UserInfoViewModel
    private lateinit var popupViewModel: PopupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIntroBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        val app = applicationContext as MyApp
        userInfoViewModel = app.userInfoViewModel
        popupViewModel = ViewModelProvider(this)[PopupViewModel::class.java]


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
                showPopup("로그인 실패", "사용자 정보를 조회하는데 실패하였습니다.", false)
                ApiClient.instance.logout()
            }
        }

        userInfoViewModel.is_username_null.observe(this) { isNull ->
            if (isNull) {
                val usernameIntent = Intent(this, UserNameActivity::class.java)
                startActivity(usernameIntent)
                finish()
            }
        }

        userInfoViewModel.user_info.observe(this) { userInfo ->
            userInfo?.let {
                Log.d(TAG, "HomeActivity로 이동")
                val homeIntent = Intent(this, HomeActivity::class.java)
                startActivity(homeIntent)
                finish()
            } ?: run {
                // error
                showPopup("로그인 실패", "사용자 정보를 조회하는데 실패하였습니다.", false)
            }
        }

        popupViewModel.popupEvent.observe(this) { event ->
            when (event) {
                "confirm" -> {
                    finish()
                }
                "cancel" -> {
                    finish()
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

    private fun showPopup(title: String, detail: String, isOneBtn: Boolean) {
        val popupData = PopupData(title, detail, isOneBtn)
        val popup = CommonPopupFragment(popupData, popupViewModel)
        popup.show(supportFragmentManager, "CommonPopup")
    }
}