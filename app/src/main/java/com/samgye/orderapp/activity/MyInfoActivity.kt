package com.samgye.orderapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.ViewModelProvider
import com.samgye.orderapp.R
import com.samgye.orderapp.Samgye
import com.samgye.orderapp.activity.viewmodel.PopupViewModel
import com.samgye.orderapp.activity.viewmodel.UserInfoViewModel
import com.samgye.orderapp.api.ApiClient
import com.samgye.orderapp.data.PopupData
import com.samgye.orderapp.databinding.ActivityMyInfoBinding
import com.samgye.orderapp.fragment.CommonPopupFragment

class MyInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyInfoBinding
    private lateinit var userInfoViewModel: UserInfoViewModel
    private lateinit var popupViewModel: PopupViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        userInfoViewModel = Samgye.userInfoViewModel
        popupViewModel = ViewModelProvider(this)[PopupViewModel::class.java]

        binding.userInfoViewModel = userInfoViewModel
        binding.lifecycleOwner = this

        binding.tvInfoSettingTry.setOnClickListener {
            val usernameIntent = Intent(this, UserNameActivity::class.java)
            startActivity(usernameIntent)
        }

        binding.tvLogout.setOnClickListener {
            showPopup("로그아웃", "로그아웃을 진행하시겠습니까?", false)
        }

        popupViewModel.popupEvent.observe(this) { event ->
            when (event) {
                "confirm" -> {
                    ApiClient.instance.logout()
                    userInfoViewModel.clearUserInfo()
                    finish()
                }
            }
        }
    }

    private fun showPopup(title: String, detail: String, isOneBtn: Boolean) {
        val popupData = PopupData(title, detail, isOneBtn)
        val popup = CommonPopupFragment(popupData, popupViewModel)
        popup.show(supportFragmentManager, "CommonPopup")
    }
}