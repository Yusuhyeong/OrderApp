package com.samgye.orderapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.samgye.orderapp.R
import com.samgye.orderapp.Samgye
import com.samgye.orderapp.activity.viewmodel.CartViewModel
import com.samgye.orderapp.activity.viewmodel.PopupViewModel
import com.samgye.orderapp.activity.viewmodel.UserInfoViewModel
import com.samgye.orderapp.adapter.CartListAdapter
import com.samgye.orderapp.data.CartMenuInfo
import com.samgye.orderapp.data.PopupData
import com.samgye.orderapp.databinding.ActivityCartBinding
import com.samgye.orderapp.fragment.CommonPopupFragment
import com.samgye.orderapp.utils.PersistentKVStore
import com.samgye.orderapp.utils.SharedPrefsWrapper

class CartActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName
    private lateinit var binding: ActivityCartBinding
    private lateinit var userInfoViewModel: UserInfoViewModel
    private lateinit var cartViewModel: CartViewModel
    private lateinit var popupViewModel: PopupViewModel
    private lateinit var cartListAdapter: CartListAdapter
    private lateinit var orderType: String
    private var isOrderFinish: Boolean = false
    private val menuKey = "menuList"
    private val appCache: PersistentKVStore = SharedPrefsWrapper(Samgye.mSharedPreferences)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCartBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]
        userInfoViewModel = Samgye.userInfoViewModel
        popupViewModel = Samgye.popupViewModel

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

            if (orderType == "s") {
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

            cartViewModel.checkCanOrder()
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
            if (list.isEmpty()) {
                cartViewModel.setIsCartExist(false)
            } else {
                cartViewModel.setIsCartExist(true)
                Log.d(TAG, list.toString())
                cartListAdapter.submitList(list)

                cartViewModel.setTotalPrice()
            }

            cartViewModel.checkCanOrder()
        }

        cartViewModel.is_can_order.observe(this) {
            val res: Int
            var isEnable = false

            if (it) {
                res = R.drawable.border_radius_state_true_12px
                isEnable = true
            } else {
                res = R.drawable.border_radius_state_false_12px
                isEnable = false
            }

            binding.tvOrder.setBackgroundResource(res)
            binding.tvOrder.isEnabled = isEnable
        }

        cartViewModel.order_state.observe(this) { state ->
            when (state) {
                "success" -> {
                    Log.d(TAG, "주문 성공")
                    isOrderFinish = true
                    appCache.remove(menuKey).commit()
                    userInfoViewModel.loadUserInfo()
                    val intent = Intent(this, OrderListActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                "server error" -> {
                    showPopup("ERROR", "주문에 문제가 생겼습니다.\n다시 시도해주세요.", true)
                }
                else -> {
                    showPopup("ERROR", state, true)
                }
            }
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

        binding.clCartOrder.setOnClickListener {
            showPopup("주문확인", "주문하시겠습니까?", false)
        }

        popupViewModel.popupEvent.observe(this) { event ->
            if (popupViewModel.popupData.value?.title != "ERROR") {
                when (event) {
                    "confirm" -> {
                        cartViewModel.orderMenu()
                    }
                }
            }
        }
    }

    private fun showPopup(title: String, detail: String, isOneBtn: Boolean) {
        val popupData = PopupData(title, detail, isOneBtn)
        val popup = CommonPopupFragment(popupData, popupViewModel)
        popup.show(supportFragmentManager, "CommonPopup")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "CartActivity onPause()")

        if (!isOrderFinish) {
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
}