package com.samgye.orderapp.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.samgye.orderapp.activity.viewmodel.LoginViewModel
import com.samgye.orderapp.api.ApiClient
import com.samgye.orderapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userNameLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.loginViewModel = viewModel
        binding.lifecycleOwner = this

        userNameLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                ApiClient.instance.userInfo() { result, error ->
                    if (error != null) {
                        // 팝업 후 finish
                        finish() // 임시 코드
                    } else if (result?.username.toString().isNotEmpty()) {
                        val resultIntent = Intent().apply {
                            putExtra("result", result)
                        }
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    }
                }
            }
        }

        viewModel.user_accesstoken.observe(this) { token ->
            if (token.equals("-1")) {
                setResult(Activity.RESULT_OK, null)
                finish()
            } else {
                ApiClient.instance.userInfo() { result, error ->
                    if (error != null) {
                        // 팝업 후 finish
                        finish() // 임시 코드
                    } else if (result?.username.toString().isNotEmpty()) {
                        val resultIntent = Intent().apply {
                            putExtra("result", result)
                        }
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    } else {
                        // username 설정 화면으로 이동
                        val userNameIntent = Intent(this, UserNameActivity::class.java)
                        userNameLauncher.launch(userNameIntent)
                    }
                }
            }
        }
    }
}