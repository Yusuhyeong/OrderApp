package com.samgye.orderapp.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.samgye.orderapp.R
import com.samgye.orderapp.activity.viewmodel.HomeViewModel
import com.samgye.orderapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName
    private lateinit var binding: ActivityHomeBinding
    private lateinit var loginLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        loginLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val resultData = result.data?.getStringExtra("result")
                if (resultData.isNullOrEmpty()) {
                    // 팝업 후 finish
                    Log.d(TAG, "data 없음")
                    finish() // 임시 코드
                } else {
                    Log.d(TAG, "data 있음")
                }
            }
        }

        val viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding.homeViewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.is_menu_visible.observe(this) { visible ->
            Log.d("HomeActivity", "menu status : $visible")

            if (visible) {
                binding.clMenu.startAnimation(AnimationUtils.loadAnimation(this, R.anim.menu_slide_in))
            } else {
                binding.clMenu.startAnimation(AnimationUtils.loadAnimation(this, R.anim.menu_slide_out))
            }
        }

        viewModel.selected_id.observe(this) { id ->
            Log.d("HomeActivity", "onClick menu : ${id.toString()}")
            when(id) {
                // id별 행동 추가
                R.id.tv_test_login.toString() -> { // 카카오 로그인
                    val loginIntent = Intent(this, LoginActivity::class.java)
                    loginLauncher.launch(loginIntent)
                }
            }
        }
    }
}