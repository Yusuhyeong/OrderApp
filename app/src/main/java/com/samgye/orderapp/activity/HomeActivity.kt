package com.samgye.orderapp.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.samgye.orderapp.data.MyData
import com.samgye.orderapp.R
import com.samgye.orderapp.activity.viewmodel.HomeViewModel
import com.samgye.orderapp.api.ApiClient
import com.samgye.orderapp.api.response.UserInfoResponse
import com.samgye.orderapp.data.NoticeInfo
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
        val viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding.homeViewModel = viewModel
        binding.lifecycleOwner = this

        // 유저 정보 확인
        if (initUserData() != null) {
            Log.d(TAG, "username : ${initUserData()?.userName}, snsType : ${initUserData()?.snsType}, point : ${initUserData()?.point}")
            val myData = MyData(initUserData()?.userName, initUserData()?.snsType, initUserData()?.point)
            viewModel.setUserData(myData)
        } else {
            Log.d(TAG, "No UserData...")
        }

        loginLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "Result Ok from LoginActivity")
                val username = result.data?.getStringExtra("username")
                val snsType = result.data?.getStringExtra("snsType")
                val point = result.data?.getIntExtra("userPoint", 0)
                if (username == null || point == null) {
                    // 팝업 후 finish
                    Log.d(TAG, "data 없음")
                    finish() // 임시 코드
                } else {
                    Log.d(TAG, "data 있음")
                    val myData = MyData(username, snsType, point)
                    viewModel.setUserData(myData)
                    Log.d(TAG, "username : ${myData.userName}, snsType : ${myData.snsType}, point : ${myData.point}")
                    binding.homeViewModel?.setLoginStatus(ApiClient.instance.hasToken())
                    viewModel.setMenuVisible(false)
                }
            }
        }

        viewModel.is_menu_visible.observe(this) { visible ->
            Log.d(TAG, "menu status : $visible")

            if (visible) {
                binding.clMenu.startAnimation(AnimationUtils.loadAnimation(this, R.anim.menu_slide_in))
            } else {
                binding.clMenu.startAnimation(AnimationUtils.loadAnimation(this, R.anim.menu_slide_out))
            }
        }

        viewModel.selected_id.observe(this) { id ->
            Log.d(TAG, "onClick menu : ${id.toString()}")
            when(id) {
                // id별 행동 추가
                R.id.cl_login_annotation.toString(), R.id.tv_menu_login_btn.toString() -> { // 카카오 로그인 (홈 화면)
                    Log.d(TAG, "로그인 클릭")
                    val loginIntent = Intent(this, LoginActivity::class.java)
                    loginLauncher.launch(loginIntent)
                }
                R.id.cl_store_eat.toString(), R.id.cl_order_in_menu.toString() -> { // 매장 식사
                    Log.d(TAG, "매장 식사 클릭")
                }
                R.id.cl_take_out.toString() -> { // 매장 식사
                    Log.d(TAG, "포장 주문 클릭")
                }
                R.id.tv_menu_logout.toString() -> { // 로그아웃
                    Log.d(TAG, "로그 아웃 클릭")
                    ApiClient.instance.logout()
                    viewModel.clearHome()
                }
                R.id.tv_menu_my_info.toString() -> { // 내정보
                    Log.d(TAG, "내정보 클릭")
                }
                R.id.cl_order_list_in_menu.toString() -> { // 주문 내역
                    Log.d(TAG, "주문 내역 클릭")
                }
                R.id.cl_find_store_in_menu.toString() -> { // 가게 찾기
                    Log.d(TAG, "가게 찾기 클릭")
                }
                R.id.cl_event_in_menu.toString() -> { // 이벤트
                    Log.d(TAG, "이벤트 클릭")
                }
                R.id.cl_notice.toString() -> {
                    Log.d(TAG, "홈 화면 공지사항 클릭")
                    val noticeIntent = Intent(this, NoticeActivity::class.java)
                    noticeIntent.putExtra("noticeSeq", viewModel.noticeData.value?.noticeSeq)
                    startActivity(noticeIntent)
                }
                R.id.cl_notice_in_menu.toString() -> {
                    Log.d(TAG, "메뉴 화면 공지사항 클릭")
                    val noticeIntent = Intent(this, NoticeActivity::class.java)
                    startActivity(noticeIntent)
                }
                R.id.cl_dummy_menu.toString() -> {
                    Log.d(TAG, "상단 메뉴 닫기")
                    viewModel.setMenuVisible(false)
                }
                R.id.iv_menu.toString() -> {
                    Log.d(TAG, "상단 메뉴 열기")
                    viewModel.setMenuVisible(true)
                }
            }
        }
    }

    private fun initUserData(): MyData? {
        val username = intent.getStringExtra("username")
        val snsType = intent.getStringExtra("snsType")
        val point = intent.getIntExtra("point", 0)

        return if (username.isNullOrEmpty() || snsType.isNullOrEmpty()) {
            Log.d(TAG, "MyData is null")
            null
        } else {
            MyData(username, snsType, point)
        }
    }
}