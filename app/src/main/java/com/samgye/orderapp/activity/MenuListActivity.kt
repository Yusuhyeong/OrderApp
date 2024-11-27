package com.samgye.orderapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.samgye.orderapp.MyApp
import com.samgye.orderapp.R
import com.samgye.orderapp.Samgye
import com.samgye.orderapp.activity.viewmodel.MenuViewModel
import com.samgye.orderapp.activity.viewmodel.PopupViewModel
import com.samgye.orderapp.activity.viewmodel.UserInfoViewModel
import com.samgye.orderapp.adapter.CategoryListAdapter
import com.samgye.orderapp.adapter.MenuListAdapter
import com.samgye.orderapp.adapter.NoticeListAdapter
import com.samgye.orderapp.api.TokenManager
import com.samgye.orderapp.data.CartMenuInfo
import com.samgye.orderapp.data.PopupData
import com.samgye.orderapp.databinding.ActivityMenuListBinding
import com.samgye.orderapp.databinding.CategoryListRvItemBinding
import com.samgye.orderapp.databinding.MenuListRvItemBinding
import com.samgye.orderapp.fragment.CommonPopupFragment
import com.samgye.orderapp.utils.PersistentKVStore
import com.samgye.orderapp.utils.SharedPrefsWrapper

class MenuListActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName
    private lateinit var binding: ActivityMenuListBinding
    private lateinit var menuViewModel: MenuViewModel
    private lateinit var popupViewModel: PopupViewModel
    private lateinit var categoryListAdapter: CategoryListAdapter
    private val appCache: PersistentKVStore = SharedPrefsWrapper(Samgye.mSharedPreferences)
    private val menuKey = "menuList"

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
                showPopup("메뉴 조회 오류", "메뉴를 가져오는데 실패했습니다.\n홈 화면으로 이동합니다.", true)
            }
        }

        binding.ivMenuListBack.setOnClickListener {
            Log.d(TAG, "menu list activity finish")
            finish()
        }

        menuViewModel.choose_list_data.observe(this) { chooseData ->
            Log.d(TAG, "menuSeq : ${chooseData.menuSeq}")
            Log.d(TAG, "menuSize : ${chooseData.menuSize}")
            Log.d(TAG, "menuImgUrl : ${chooseData.menuImgUrl}")
            Log.d(TAG, "menuPrice : ${chooseData.menuPrice}")
            Log.d(TAG, "menuTitle : ${chooseData.menuTitle}")

            val chooseMenuIntent = Intent(this, ChooseMenuActivity::class.java)
            chooseMenuIntent.putExtra("menuTitle", chooseData.menuTitle)
            chooseMenuIntent.putExtra("menuInfo", chooseData.menuInfo)
            chooseMenuIntent.putExtra("menuSeq", chooseData.menuSeq)
            chooseMenuIntent.putExtra("menuSize", chooseData.menuSize)
            chooseMenuIntent.putExtra("menuImgUrl", chooseData.menuImgUrl)
            chooseMenuIntent.putExtra("menuPrice", chooseData.menuPrice)
            startActivity(chooseMenuIntent)
        }

        menuViewModel.cart_menu_info.observe(this) { cartList ->
            var isEnable = false
            var res: Int

            if (cartList != null) {
                isEnable = true
                res = R.drawable.border_radius_state_true_12px
                for (cart in cartList) {
                    Log.d(TAG, "=====================================")
                    Log.d(TAG, "menuSeq : ${cart.menuSeq}")
                    Log.d(TAG, "menuSize : ${cart.menuSize}")
                    Log.d(TAG, "=====================================")
                }
            } else {
                Log.d(TAG, "No cart data available")
                isEnable = false
                res = R.drawable.border_radius_state_false_12px
            }

            binding.tvOrder.setBackgroundResource(res)
            binding.tvOrder.isEnabled = isEnable
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

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "MenuListActivity onResume")
        val gson = Gson()
        val cartJson = appCache.getString(menuKey, null)
        if (cartJson != null) {
            Log.d(TAG, "load cart data")
            menuViewModel.loadCartMenu(gson.fromJson(cartJson, object : TypeToken<List<CartMenuInfo>>() {}.type))

            // appCache.remove(menuKey).commit()는 현재 테스트용이므로 결제화면 구현시 주석처리
            appCache.remove(menuKey).commit()
        } else {
            Log.d(TAG, "no cart data")
        }
    }

}