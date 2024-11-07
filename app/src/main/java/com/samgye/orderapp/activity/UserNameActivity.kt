package com.samgye.orderapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.samgye.orderapp.R
import com.samgye.orderapp.api.ApiClient
import com.samgye.orderapp.api.request.UsernameRequest

class UserNameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_name)

        val userInfoRequest = UsernameRequest(username = "거지에용")

        ApiClient.instance.updateUserName(userInfoRequest) { data, error ->
            if (error != null) {
                Log.d("TEST", "ERROR")
            } else {
                if (data != null) {
                    if (data.code == 2000) {
                        Log.d("TEST", "SUCCESS")
                    } else {
                        Log.d("TEST", "NOT SUCCESS")
                    }
                }
            }
        }
    }
}