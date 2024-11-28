package com.samgye.orderapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.samgye.orderapp.MyApp
import com.samgye.orderapp.R
import com.samgye.orderapp.Samgye
import com.samgye.orderapp.activity.viewmodel.CartViewModel
import com.samgye.orderapp.activity.viewmodel.UserInfoViewModel
import com.samgye.orderapp.adapter.CartListAdapter
import com.samgye.orderapp.data.CartMenuInfo
import com.samgye.orderapp.databinding.ActivityCartBinding
import com.samgye.orderapp.utils.PersistentKVStore
import com.samgye.orderapp.utils.SharedPrefsWrapper

class CartActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName
    private lateinit var binding: ActivityCartBinding
    private lateinit var userInfoViewModel: UserInfoViewModel
    private lateinit var cartViewModel: CartViewModel
    private lateinit var cartListAdapter: CartListAdapter
    private val menuKey = "menuList"
    private val appCache: PersistentKVStore = SharedPrefsWrapper(Samgye.mSharedPreferences)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        binding = ActivityCartBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]
        val app = applicationContext as MyApp
        userInfoViewModel = app.userInfoViewModel

        binding.cartViewModel = cartViewModel
        binding.userViewModel = userInfoViewModel
        binding.lifecycleOwner = this

        cartListAdapter = CartListAdapter(cartViewModel)
        binding.rvCartList.adapter = cartListAdapter

        val gson = Gson()
        val cartJson = appCache.getString(menuKey, null)

        if (cartJson != null) {
            Log.d(TAG, "load cart data")
            cartViewModel.loadCartMenu(gson.fromJson(cartJson, object : TypeToken<List<CartMenuInfo>>() {}.type))
        } else {
            Log.d(TAG, "no cart data")
        }

        cartViewModel.cart_menu_info.observe(this) { list ->
            if (list.isEmpty()) {
                // 비어있을 때, 비어있는 화면 보여주기
                cartViewModel.setIsCartExist(false)
            } else {
                // 주문 화면 보여주기
                cartViewModel.setIsCartExist(true)
                Log.d(TAG, list.toString())
                cartListAdapter.submitList(list)
            }
        }
    }
}