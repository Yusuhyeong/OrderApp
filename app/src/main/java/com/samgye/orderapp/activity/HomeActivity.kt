package com.samgye.orderapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import com.samgye.orderapp.MyApp
import com.samgye.orderapp.R
import com.samgye.orderapp.activity.viewmodel.HomeViewModel
import com.samgye.orderapp.activity.viewmodel.UserInfoViewModel
import com.samgye.orderapp.api.ApiClient
import com.samgye.orderapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName
    private lateinit var binding: ActivityHomeBinding
    private lateinit var userInfoViewModel: UserInfoViewModel
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        val app = applicationContext as MyApp
        userInfoViewModel = app.userInfoViewModel

        binding.homeViewModel = homeViewModel
        binding.userViewModel = userInfoViewModel
        binding.lifecycleOwner = this

        homeViewModel.is_menu_visible.observe(this) { visible ->
            Log.d(TAG, "menu status : $visible")

            if (visible) {
                binding.clMenu.startAnimation(AnimationUtils.loadAnimation(this, R.anim.menu_slide_in))
            } else {
                binding.clMenu.startAnimation(AnimationUtils.loadAnimation(this, R.anim.menu_slide_out))
            }
        }

        homeViewModel.selected_id.observe(this) { id ->
            Log.d(TAG, "onClick menu : ${id.toString()}")

            when(id) {
                // id별 행동 추가
                R.id.cl_login_annotation.toString(), R.id.tv_menu_login_btn.toString() -> { // 카카오 로그인 (홈 화면)
                    Log.d(TAG, "로그인 클릭")
                    if (!ApiClient.instance.hasToken()) {
                        val loginIntent = Intent(this, LoginActivity::class.java)
                        startActivity(loginIntent)
                    }
                }
                R.id.cl_store_eat.toString(), R.id.cl_order_in_menu.toString() -> { // 매장 식사
                    Log.d(TAG, "매장 식사 클릭")
                    val menuListIntent = Intent(this, MenuListActivity::class.java)
                    startActivity(menuListIntent)
                }
                R.id.cl_take_out.toString() -> { // 포장 주문
                    Log.d(TAG, "포장 주문 클릭")
                    val menuListIntent = Intent(this, MenuListActivity::class.java)
                    startActivity(menuListIntent)
                }
                R.id.tv_menu_logout.toString() -> { // 로그아웃
                    Log.d(TAG, "로그 아웃 클릭")
                    ApiClient.instance.logout()
                    userInfoViewModel.clearUserInfo()
                    homeViewModel.setMenuVisible(false)
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
                    noticeIntent.putExtra("noticeSeq", homeViewModel.noticeData.value?.noticeSeq)
                    startActivity(noticeIntent)
                }
                R.id.cl_notice_in_menu.toString() -> {
                    Log.d(TAG, "메뉴 화면 공지사항 클릭")
                    val noticeIntent = Intent(this, NoticeActivity::class.java)
                    startActivity(noticeIntent)
                }
                R.id.cl_dummy_menu.toString() -> {
                    Log.d(TAG, "상단 메뉴 닫기")
                    homeViewModel.setMenuVisible(false)
                }
                R.id.iv_menu.toString() -> {
                    Log.d(TAG, "상단 메뉴 열기")
                    homeViewModel.setMenuVisible(true)
                }
            }
        }
    }
}