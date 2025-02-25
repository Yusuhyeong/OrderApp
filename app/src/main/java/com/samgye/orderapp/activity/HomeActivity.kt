package com.samgye.orderapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.samgye.orderapp.R
import com.samgye.orderapp.Samgye
import com.samgye.orderapp.activity.viewmodel.HomeViewModel
import com.samgye.orderapp.activity.viewmodel.UserInfoViewModel
import com.samgye.orderapp.adapter.EventListAdapter
import com.samgye.orderapp.api.ApiClient
import com.samgye.orderapp.data.EventInfo
import com.samgye.orderapp.databinding.ActivityHomeBinding
import com.samgye.orderapp.fragment.AlertFragment

class HomeActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName
    private lateinit var binding: ActivityHomeBinding
    private lateinit var userInfoViewModel: UserInfoViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var eventListAdapter: EventListAdapter
    private var firstHome: Boolean = false
    private val handler = Handler(Looper.getMainLooper())
    private var currentPage = 0
    private var totalPage = 0
    private var eventListSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        userInfoViewModel = Samgye.userInfoViewModel

        binding.homeViewModel = homeViewModel
        binding.userViewModel = userInfoViewModel
        binding.lifecycleOwner = this

        eventListAdapter = EventListAdapter(homeViewModel, this)
        binding.vpEvent.adapter = eventListAdapter
        binding.vpEvent.registerOnPageChangeCallback(pageChangeCallback)

        homeViewModel.loadEventInfo()

        homeViewModel.event_list.observe(this) { baseEventList ->
            if (baseEventList != null) {
                Log.d(TAG, "eventList size : ${baseEventList.size}")
                eventListSize = baseEventList.size

                val eventList: MutableList<EventInfo> = mutableListOf()
                for (i: Int in 0..<50) {
                    for (element in baseEventList) {
                        eventList.add(element)
                    }
                }
                totalPage = eventList.size

                eventListAdapter.submitList(eventList)
                currentPage = eventListSize * 25 + 1
                homeViewModel.setCurrentPage(currentPage, eventListSize)
                homeViewModel.setTotalPage(eventListSize)
                binding.vpEvent.setCurrentItem(currentPage, true)
                startAutoScroll()
            }
        }

        // event banner click event 처리
        homeViewModel.click_event_info.observe(this) { eventInfo ->
            if (eventInfo != null) {
                Log.d(TAG, "eventTitle : ${eventInfo.eventTitle}")
                Log.d(TAG, "eventCont : ${eventInfo.eventCont}")
                Log.d(TAG, "eventSeq : ${eventInfo.eventSeq}")
                Log.d(TAG, "eventDttm : ${eventInfo.eventDttm}")
                Log.d(TAG, "eventUuid : ${eventInfo.eventUuid}")
                Log.d(TAG, "eventImg : ${eventInfo.eventImg}")
            }
        }

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
                    if (!ApiClient.instance.hasToken()) {
                        AlertFragment()
                            .setTitle("로그인")
                            .setMessage("로그인 후\n" +
                                    "해당 서비스를 이용해주세요.")
                            .setIsOneBtn(true)
                            .setPositiveButton {
                                val loginIntent = Intent(this, LoginActivity::class.java)
                                startActivity(loginIntent)
                            }
                            .show(supportFragmentManager, "AlertFragment")
                    } else {
                        val menuListIntent = Intent(this, MenuListActivity::class.java)
                        menuListIntent.putExtra("title", "매장 식사")
                        startActivity(menuListIntent)
                    }
                }
                R.id.cl_take_out.toString() -> { // 포장 주문
                    Log.d(TAG, "포장 주문 클릭")
                    if (!ApiClient.instance.hasToken()) {
                        AlertFragment()
                            .setTitle("로그인")
                            .setMessage("로그인 후\n" +
                                    "해당 서비스를 이용해주세요.")
                            .setIsOneBtn(true)
                            .setPositiveButton {
                                val loginIntent = Intent(this, LoginActivity::class.java)
                                startActivity(loginIntent)
                            }
                            .show(supportFragmentManager, "AlertFragment")
                    } else {
                        val menuListIntent = Intent(this, MenuListActivity::class.java)
                        menuListIntent.putExtra("title", "포장 주문")
                        startActivity(menuListIntent)
                    }
                }
                R.id.tv_menu_logout.toString() -> { // 로그아웃
                    Log.d(TAG, "로그 아웃 클릭")
                    AlertFragment()
                        .setTitle("로그아웃")
                        .setMessage("로그아웃을 진행하시겠습니까?")
                        .setIsOneBtn(false)
                        .setPositiveButton {
                            ApiClient.instance.logout()
                            userInfoViewModel.clearUserInfo()
                            homeViewModel.setMenuVisible(false)
                        }
                        .show(supportFragmentManager, "AlertFragment")
                }
                R.id.tv_menu_my_info.toString() -> { // 내정보
                    Log.d(TAG, "내정보 클릭")
                    val myInfoIntent = Intent(this, MyInfoActivity::class.java)
                    startActivity(myInfoIntent)
                }
                R.id.cl_order_list_in_menu.toString() -> { // 주문 내역
                    Log.d(TAG, "주문 내역 클릭")
                    if (!ApiClient.instance.hasToken()) {
                        AlertFragment()
                            .setTitle("로그인")
                            .setMessage("로그인 후\n" +
                                    "해당 서비스를 이용해주세요.")
                            .setIsOneBtn(true)
                            .setPositiveButton {
                                val loginIntent = Intent(this, LoginActivity::class.java)
                                startActivity(loginIntent)
                            }
                            .show(supportFragmentManager, "AlertFragment")
                    } else {
                        // 추가
                        val intent = Intent(this, OrderListActivity::class.java)
                        startActivity(intent)
                    }
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
                    Log.d(TAG, "메뉴 화면 클릭")
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

    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            currentPage = position
            homeViewModel.setCurrentPage(currentPage, eventListSize)
            startAutoScroll()
        }
    }

    private fun startAutoScroll() {
        handler.removeCallbacksAndMessages(null)

        val runnable = object : Runnable {
            override fun run() {
                if (totalPage > 0) {
                    currentPage += 1
                    binding.vpEvent.setCurrentItem(currentPage, true)
                    handler.postDelayed(this, 3000)
                }
            }
        }
        handler.postDelayed(runnable, 3000)
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "HomeActivity onResume")
        if (firstHome) {
            userInfoViewModel.loadUserInfo()
        } else {
            firstHome = true
        }
    }
}