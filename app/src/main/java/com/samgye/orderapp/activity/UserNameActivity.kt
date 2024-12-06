package com.samgye.orderapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.samgye.orderapp.R
import com.samgye.orderapp.Samgye
import com.samgye.orderapp.activity.viewmodel.PopupViewModel
import com.samgye.orderapp.activity.viewmodel.UserInfoViewModel
import com.samgye.orderapp.api.ApiClient
import com.samgye.orderapp.api.request.UsernameRequest
import com.samgye.orderapp.data.PopupData
import com.samgye.orderapp.databinding.ActivityUserNameBinding
import com.samgye.orderapp.fragment.CommonPopupFragment

class UserNameActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName
    private lateinit var binding: ActivityUserNameBinding
    private lateinit var userInfoViewModel: UserInfoViewModel
    private lateinit var popupViewModel: PopupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserNameBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        userInfoViewModel = Samgye.userInfoViewModel
        popupViewModel = ViewModelProvider(this)[PopupViewModel::class.java]

        binding.userViewModel = userInfoViewModel
        binding.lifecycleOwner = this

        userInfoViewModel.username_api_state.observe(this) { state ->
            if (state != null) {
                if (state == "1") {
                    val username = userInfoViewModel.username_value.value
                    showPopup("설정 완료", "사용자 닉네임을\n${username}으로 저장하였습니다.", true)
                } else if (state == "error") {
                    showPopup("server error", "앱 종료 후\n다시 이용해주세요.", true)
                } else {
                    showPopup("설정 실패", "사용자 닉네임을\n저장하는데 실패하였습니다.", true)
                }
            } else {
                showPopup("server error", "앱 종료 후\n다시 이용해주세요.", true)
            }
        }

        popupViewModel.popupEvent.observe(this) { event ->
            when (event) {
                "confirm" -> {
                    if (userInfoViewModel.username_value.value.isNullOrEmpty()) {
                        finish()
                    } else {
                        Handler().postDelayed(Runnable {
                            userInfoViewModel.loadUserInfo()
                            val homeIntent = Intent(this, HomeActivity::class.java)
                            startActivity(homeIntent)
                            finish()
                        }, 1000)
                    }
                }
            }
        }

        binding.etUsernameSet.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                var isEnable = false
                var res: Int

                if (!s.isNullOrEmpty()) {
                    userInfoViewModel.setUsernameValue(s.toString())
                    res = R.drawable.border_radius_state_true_12px
                    isEnable = true
                } else {
                    userInfoViewModel.setUsernameValue("")
                    res = R.drawable.border_radius_state_false_12px
                    isEnable = false
                }

                binding.tvUsernameStore.setBackgroundResource(res)
                binding.tvUsernameStore.isEnabled = isEnable
            }
        })

        binding.ivUsernameBack.setOnClickListener {
            val username = userInfoViewModel.username_value.value
            if (username.isNullOrEmpty()) {
                Log.d(TAG, "username $username")
                showPopup("설정 확인", "닉네임 없이\n서비스를 이용하실 수 없습니다.", false)
            } else {
                Log.d(TAG, "username $username")
                finish()
            }
        }
    }

    private fun showPopup(title: String, detail: String, isOneBtn: Boolean) {
        val popupData = PopupData(title, detail, isOneBtn)
        val popup = CommonPopupFragment(popupData, popupViewModel)
        popup.show(supportFragmentManager, "CommonPopup")
    }
}