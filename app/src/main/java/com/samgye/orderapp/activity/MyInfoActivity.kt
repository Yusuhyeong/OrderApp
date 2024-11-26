package com.samgye.orderapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.samgye.orderapp.MyApp
import com.samgye.orderapp.R
import com.samgye.orderapp.activity.viewmodel.UserInfoViewModel
import com.samgye.orderapp.databinding.ActivityMyInfoBinding

class MyInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyInfoBinding
    private lateinit var userInfoViewModel: UserInfoViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_info)

        binding = ActivityMyInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val app = applicationContext as MyApp
        userInfoViewModel = app.userInfoViewModel

        binding.userInfoViewModel = userInfoViewModel
        binding.lifecycleOwner = this
    }
}