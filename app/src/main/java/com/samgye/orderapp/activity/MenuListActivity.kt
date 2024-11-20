package com.samgye.orderapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.samgye.orderapp.MyApp
import com.samgye.orderapp.activity.viewmodel.MenuViewModel
import com.samgye.orderapp.activity.viewmodel.PopupViewModel
import com.samgye.orderapp.activity.viewmodel.UserInfoViewModel
import com.samgye.orderapp.adapter.CategoryListAdapter
import com.samgye.orderapp.adapter.MenuListAdapter
import com.samgye.orderapp.adapter.NoticeListAdapter
import com.samgye.orderapp.data.PopupData
import com.samgye.orderapp.databinding.ActivityMenuListBinding
import com.samgye.orderapp.databinding.CategoryListRvItemBinding
import com.samgye.orderapp.databinding.MenuListRvItemBinding
import com.samgye.orderapp.fragment.CommonPopupFragment

class MenuListActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName
    private lateinit var binding: ActivityMenuListBinding
    private lateinit var menuViewModel: MenuViewModel
    private lateinit var popupViewModel: PopupViewModel
    private lateinit var categoryListAdapter: CategoryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuListBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        menuViewModel = ViewModelProvider(this)[MenuViewModel::class.java]
        popupViewModel = ViewModelProvider(this)[PopupViewModel::class.java]

        binding.menuViewModel = menuViewModel
        binding.lifecycleOwner = this

        categoryListAdapter = CategoryListAdapter(menuViewModel)
        binding.rvMenuList.adapter = categoryListAdapter


        menuViewModel.loadMenuData(intent.getStringExtra("title").toString())

        menuViewModel.menu_data.observe(this) { menuData ->
            menuData?.let {
                Log.d(TAG, "========================menu========================")
                for(i: Int in menuData.indices) {
                    Log.d(TAG, "categorySeq : ${menuData[i].categorySeq}")
                    Log.d(TAG, "categoryNm : ${menuData[i].categoryNm}")
                    for (j : Int in 0..<menuData[i].menu!!.size) {
                        Log.d(TAG, "menuSeq : ${menuData[i].menu?.get(j)?.menuSeq}")
                        Log.d(TAG, "menuTitle : ${menuData[i].menu?.get(j)?.menuTitle}")
                        Log.d(TAG, "menuInfo : ${menuData[i].menu?.get(j)?.menuInfo}")
                        Log.d(TAG, "menuImgUrl : ${menuData[i].menu?.get(j)?.menuImgUrl}")
                        Log.d(TAG, "menuPrice : ${menuData[i].menu?.get(j)?.menuPrice}")
                        Log.d(TAG, "popularYn : ${menuData[i].menu?.get(j)?.popularYn}")
                    }
                }
                Log.d(TAG, "====================================================")

                categoryListAdapter.submitList(menuData)
            } ?: run {
                // error
                showPopup("메뉴 조회 오류", "메뉴를 가져오는데 실패했습니다.\n홈 화면으로 이동합니다.", false)
            }
        }

        popupViewModel.popupEvent.observe(this) { event ->
            when (event) {
                "confirm" -> {
                    finish()
                }
                "cancel" -> {
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