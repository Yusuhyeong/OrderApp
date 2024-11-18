package com.samgye.orderapp.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.samgye.orderapp.MyApp
import com.samgye.orderapp.activity.viewmodel.LoginViewModel
import com.samgye.orderapp.activity.viewmodel.UserInfoViewModel
import com.samgye.orderapp.api.ApiClient
import com.samgye.orderapp.databinding.ActivityIntroBinding

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
        val app = applicationContext as MyApp
        userInfoViewModel = app.userInfoViewModel


        if (isNetworkConnected()) {
            if (ApiClient.instance.hasToken()) {
                loginViewModel.refreshToken()
            } else {
                val homeIntent = Intent(this, HomeActivity::class.java)
                startActivity(homeIntent)
            }
        }

        loginViewModel.is_token_refresh.observe(this) { refresh ->
            if (refresh) {
                userInfoViewModel.loadUserInfo()
            } else {
                // error
                finish()
            }
        }

        userInfoViewModel.is_username_null.observe(this) { isNull ->
            if (isNull) {
                val usernameIntent = Intent(this, UserNameActivity::class.java)
                startActivity(usernameIntent)
            }
        }

        userInfoViewModel.user_info.observe(this) { userInfo ->
            userInfo?.let {
                val homeIntent = Intent(this, HomeActivity::class.java)
                startActivity(homeIntent)
            } ?: run {
                // error
                finish()
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