package com.samgye.orderapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.samgye.orderapp.R
import com.samgye.orderapp.Samgye
import com.samgye.orderapp.activity.viewmodel.UserInfoViewModel
import com.samgye.orderapp.databinding.ActivityUserNameBinding
import com.samgye.orderapp.fragment.AlertFragment

class UserNameActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName
    private lateinit var binding: ActivityUserNameBinding
    private lateinit var userInfoViewModel: UserInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserNameBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        userInfoViewModel = Samgye.userInfoViewModel

        binding.userViewModel = userInfoViewModel
        binding.lifecycleOwner = this

        userInfoViewModel.setUsernameValue("")

        userInfoViewModel.username_api_state.observe(this) { state ->
            if (state != null) {
                if (state == "1") {
                    val username = userInfoViewModel.username_value.value
                    // error
                    AlertFragment()
                        .setTitle("설정 완료")
                        .setMessage("사용자 닉네임을\n" +
                                "${username}으로 저장하였습니다.")
                        .setIsOneBtn(true)
                        .setPositiveButton {
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
                        .show(supportFragmentManager, "AlertFragment")
                } else if (state == "error") {
                    AlertFragment()
                        .setTitle("server error")
                        .setMessage("앱 종료 후\n" +
                                "다시 이용해주세요.")
                        .setIsOneBtn(true)
                        .setPositiveButton {
                            finish()
                        }
                        .show(supportFragmentManager, "AlertFragment")
                } else {
                    AlertFragment()
                        .setTitle("설정 실패")
                        .setMessage("사용자 닉네임을\n" +
                                "저장하는데 실패하였습니다.")
                        .setIsOneBtn(true)
                        .setPositiveButton {
                            finish()
                        }
                        .show(supportFragmentManager, "AlertFragment")
                }
            } else {
                AlertFragment()
                    .setTitle("server error")
                    .setMessage("앱 종료 후\n" +
                            "다시 이용해주세요.")
                    .setIsOneBtn(true)
                    .setPositiveButton {
                        finish()
                    }
                    .show(supportFragmentManager, "AlertFragment")
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
            val username = userInfoViewModel.user_info.value?.userName
            if (username.isNullOrEmpty()) {
                Log.d(TAG, "username $username")
                AlertFragment()
                    .setTitle("설정 확인")
                    .setMessage("닉네임 없이\n" +
                            "서비스를 이용하실 수 없습니다.")
                    .setIsOneBtn(false)
                    .setPositiveButton {
                        finish()
                    }
                    .show(supportFragmentManager, "AlertFragment")
            } else {
                Log.d(TAG, "username $username")
                userInfoViewModel.setUsernameValue(username)
                binding.etUsernameSet.setText(username)
                finish()
            }
        }
    }

    override fun onBackPressed() {
        val username = userInfoViewModel.user_info.value?.userName
        if (username.isNullOrEmpty()) {
            Log.d(TAG, "username $username")
            AlertFragment()
                .setTitle("설정 확인")
                .setMessage("닉네임 없이\n" +
                        "서비스를 이용하실 수 없습니다.")
                .setIsOneBtn(false)
                .setPositiveButton {
                    finish()
                }
                .show(supportFragmentManager, "AlertFragment")
        } else {
            userInfoViewModel.setUsernameValue(username)
            binding.etUsernameSet.setText(username)
            super.onBackPressed()
        }
    }
}