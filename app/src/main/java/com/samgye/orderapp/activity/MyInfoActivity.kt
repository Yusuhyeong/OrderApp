package com.samgye.orderapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.samgye.orderapp.Samgye
import com.samgye.orderapp.activity.viewmodel.UserInfoViewModel
import com.samgye.orderapp.api.ApiClient
import com.samgye.orderapp.databinding.ActivityMyInfoBinding
import com.samgye.orderapp.fragment.AlertFragment

class MyInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyInfoBinding
    private lateinit var userInfoViewModel: UserInfoViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        userInfoViewModel = Samgye.userInfoViewModel

        binding.userInfoViewModel = userInfoViewModel
        binding.lifecycleOwner = this

        binding.tvInfoSettingTry.setOnClickListener {
            val usernameIntent = Intent(this, UserNameActivity::class.java)
            startActivity(usernameIntent)
        }

        binding.tvLogout.setOnClickListener {
            AlertFragment()
                .setTitle("로그아웃")
                .setMessage("로그아웃을 진행하시겠습니까?")
                .setIsOneBtn(false)
                .setPositiveButton {
                    ApiClient.instance.logout()
                    userInfoViewModel.clearUserInfo()
                    finish()
                }
                .show(supportFragmentManager, "AlertFragment")
        }
    }
}