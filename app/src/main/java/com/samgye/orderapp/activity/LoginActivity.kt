package com.samgye.orderapp.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.samgye.orderapp.R
import com.samgye.orderapp.Samgye
import com.samgye.orderapp.activity.viewmodel.LoginViewModel
import com.samgye.orderapp.api.ApiClient
import com.samgye.orderapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userNameLauncher: ActivityResultLauncher<Intent>
    private lateinit var googleSignInClient: GoogleSignInClient

    // google login callback
    private val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            if (account.id?.isNotEmpty() == true) {
                Log.d(TAG, account.id.toString())
                login(account.id.toString(), "google")
            }
            Log.d(TAG, "google login success")
        } catch (e: ApiException) {
            // Google 로그인 실패
            Log.d(TAG, "google login fail")
        }
    }

    // kakao login callback
    private val kakaoCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(TAG, "카카오 계정으로 로그인 실패! " + error.message)
            Log.d(TAG, "로그인 실패")
        } else if (token != null) {
            Log.d(TAG, "카카오 계정으로 로그인 성공! " + token.accessToken)
            Log.d(TAG, "로그인 성공")

            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    Log.e(TAG, "사용자 정보 요청 실패: ${error.message}")
                } else if (user != null) {
                    Log.d(TAG, "사용자 ID: ${user.id}")

                    val userId = user.id.toString()
                    Log.d(TAG, "uid : $userId")

                    login(userId, "kakao")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.loginViewModel = viewModel
        binding.lifecycleOwner = this

        // google login 초기 셋팅
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode(getString(R.string.default_web_client_id))
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.tvTestGoogle.setOnClickListener {
            googleLoginCheck()
        }

        binding.tvTestKakao.setOnClickListener {
            kakaoLoginCheck()
        }

        userNameLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                ApiClient.instance.userInfo() { result, error ->
                    if (error != null) {
                        // 팝업 후 finish
                        finish() // 임시 코드
                    } else if (result?.username != null) {
                        val resultIntent = Intent().apply {
                            putExtra("result", result)
                        }

                        ApiClient.instance.userPointInfo() {point, error ->
                            if (error != null) {
                                // 팝업 후 finish
                                finish() // 임시 코드
                            } else {
                                resultIntent.putExtra("userPoint", point?.point)

                                setResult(Activity.RESULT_OK, resultIntent)
                                finish()
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * google login
     */
    private fun googleLoginCheck() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    /**
     * kakao login
     */
    fun kakaoLoginCheck() {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(Samgye.applicationContext)) { // 카카오톡 app 설치 유무 확인
            UserApiClient.instance.loginWithKakaoTalk(Samgye.applicationContext) { token, error ->
                if (error != null) {
                    Log.d(TAG, "카카오톡으로 로그인 실패! " + error.message)

                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    UserApiClient.instance.loginWithKakaoAccount(
                        Samgye.applicationContext,
                        callback = kakaoCallback
                    )

                } else if (token != null) {
                    Log.d(TAG, "카카오톡으로 로그인 성공! " + token.accessToken)
                    UserApiClient.instance.me { user, error ->
                        if (error != null) {
                            Log.e(TAG, "사용자 정보 요청 실패: ${error.message}")
                        } else if (user != null) {
                            Log.d(TAG, "사용자 ID: ${user.id}")

                            val userId = user.id.toString()
                            Log.d(TAG, "uid : $userId")

                            login(userId, "kakao")
                        }
                    }
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(
                Samgye.applicationContext,
                callback = kakaoCallback
            )
        }
    }

    /**
     * sns login 후 성공적으로 마쳤을 때, 실제 서버를 통한 login
     */
    private fun login(userId: String, type: String) {
        ApiClient.instance.login(userId, type) { result, error ->
            if (error != null) {
                // 에러 처리
                Log.e(TAG, "Login failed: ${error.message}")
                checkUserName("-1")
            } else if (result != null) {
                // 성공적으로 로그인한 경우
                Log.d(TAG, "api success")
                checkUserName(result.accessToken)
            }
        }
    }

    /**
     * 서버 login을 마치고 username 유무 확인
     */
    private fun checkUserName(tokenState: String) {
        if (tokenState == "-1") {
            setResult(Activity.RESULT_OK, null)
            finish()
        } else {
            ApiClient.instance.userInfo() { result, error ->
                if (error != null) {
                    // 팝업 후 finish
                    finish() // 임시 코드
                } else if (result?.username != null) {
                    val resultIntent = Intent().apply {
                        putExtra("result", result)
                    }

                    ApiClient.instance.userPointInfo() {point, error ->
                        if (error != null) {
                            // 팝업 후 finish
                            finish() // 임시 코드
                        } else {
                            resultIntent.putExtra("userPoint", point?.point)

                            setResult(Activity.RESULT_OK, resultIntent)
                            finish()
                        }
                    }
                } else {
                    // username 설정 화면으로 이동
                    val userNameIntent = Intent(this, UserNameActivity::class.java)
                    userNameLauncher.launch(userNameIntent)
                }
            }
        }
    }
}