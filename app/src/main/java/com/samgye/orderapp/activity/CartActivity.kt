package com.samgye.orderapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.core.content.ContextCompat
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
    private lateinit var orderType: String
    private val menuKey = "menuList"
    private val appCache: PersistentKVStore = SharedPrefsWrapper(Samgye.mSharedPreferences)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        orderType = intent.getStringExtra("title").toString()

        Log.d(TAG, "orderType: $orderType")

        if (orderType == "매장 식사") {
            cartViewModel.setOrderType(true)
        } else {
            cartViewModel.setOrderType(false)
        }

        cartViewModel.order_type.observe(this) { orderType ->
            val storeStrokeRes: Int
            val takeoutStrokeRes: Int
            val storeFontRes: Int
            val takeoutFontRes: Int

            if (orderType == "매장 식사") {
                storeStrokeRes = R.drawable.border_radius_black_stroke_5dp
                takeoutStrokeRes = R.drawable.border_radius_gray_stroke_5dp
                storeFontRes = ContextCompat.getColor(this, R.color.font)
                takeoutFontRes = ContextCompat.getColor(this, R.color.font_gray)
            } else {
                storeStrokeRes = R.drawable.border_radius_gray_stroke_5dp
                takeoutStrokeRes = R.drawable.border_radius_black_stroke_5dp
                storeFontRes = ContextCompat.getColor(this, R.color.font_gray)
                takeoutFontRes = ContextCompat.getColor(this, R.color.font)
            }

            binding.clOrderTypeStoreEat.setBackgroundResource(storeStrokeRes)
            binding.clOrderTypeTakeOut.setBackgroundResource(takeoutStrokeRes)
            binding.tvOrderTypeStoreEat.setTextColor(storeFontRes)
            binding.tvOrderTypeTakeOut.setTextColor(takeoutFontRes)
        }

        val gson = Gson()
        val cartJson = appCache.getString(menuKey, null)

        if (cartJson != null) {
            Log.d(TAG, "load cart data")
            cartViewModel.loadCartMenu(gson.fromJson(cartJson, object : TypeToken<List<CartMenuInfo>>() {}.type))
            cartViewModel.setTotalPrice()
        } else {
            Log.d(TAG, "no cart data")
            cartViewModel.loadCartMenu(emptyList())
        }

        cartViewModel.cart_menu_list.observe(this) { list ->
            val res: Int
            var isEnable = false

            if (list.isEmpty()) {
                // 비어있을 때, 비어있는 화면 보여주기
                res = R.drawable.border_radius_state_false_12px
                isEnable = false
                cartViewModel.setIsCartExist(false)
            } else {
                // 주문 화면 보여주기
                res = R.drawable.border_radius_state_true_12px
                isEnable = true
                cartViewModel.setIsCartExist(true)
                Log.d(TAG, list.toString())
                cartListAdapter.submitList(list)

                cartViewModel.setTotalPrice()
            }

            binding.tvOrder.setBackgroundResource(res)
            binding.tvOrder.isEnabled = isEnable
        }

        binding.ivCartBack.setOnClickListener {
            finish()
        }

        binding.clCartAdd.setOnClickListener {
            finish()
        }

        binding.etUsePoint.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty()) {
                    val currentPoint = s.toString().toInt()
                    val maxPoint = userInfoViewModel.user_info.value?.point ?: 0

                    if (currentPoint > maxPoint) {
                        cartViewModel.setUsePoint(maxPoint.toString())
                        binding.etUsePoint.setText(maxPoint.toString())
                        binding.etUsePoint.setSelection(maxPoint.toString().length)
                    } else {
                        cartViewModel.setUsePoint(s.toString())
                    }
                } else {
                    cartViewModel.setUsePoint("0")
                }
            }

        })
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "CartActivity onPause()")

        val cartMenuList = cartViewModel.cart_menu_list.value

        if (cartMenuList?.isEmpty() == true) {
            Log.d(TAG, "empty, remove")

            appCache.remove(menuKey)
            appCache.commit()
        } else {
            Log.d(TAG, "not empty, save")

            val gson = Gson()
            val cartJson = gson.toJson(cartMenuList)

            appCache.putString(menuKey, cartJson)
            appCache.commit()
        }
    }
}