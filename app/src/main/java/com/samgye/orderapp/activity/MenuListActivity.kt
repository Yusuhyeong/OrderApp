package com.samgye.orderapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.samgye.orderapp.R
import com.samgye.orderapp.api.ApiClient

class MenuListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_list)

        ApiClient.instance.getMenuInfo() { data, error ->
            if (error != null) {
                Log.d("MENU_TEST", "getMenu fail")
            } else {
                Log.d("MENU_TEST", "getMenu success")

                if (data != null) {
                    for (i in data.indices) {
                        Log.d("MENU_TEST", "************************************")
                        Log.d("MENU_TEST", "seq : ${data[i].categorySeq}")
                        Log.d("MENU_TEST", "seq : ${data[i].categoryNm}")
                        Log.d("MENU_TEST", "************************************")
                        Log.d("MENU_TEST", "====================================================")
                        for (j in 0..<data[i].menu!!.size) {
//                        for (j in 0..(data[i].menu?.size ?: 0)) {
                            Log.d("MENU_TEST", "menuSeq : ${data[i].menu?.get(j)?.menuSeq}")
                            Log.d("MENU_TEST", "menuTitle : ${data[i].menu?.get(j)?.menuTitle}")
                            Log.d("MENU_TEST", "menuInfo : ${data[i].menu?.get(j)?.menuInfo}")
                            Log.d("MENU_TEST", "menuImgUrl : ${data[i].menu?.get(j)?.menuImgUrl}")
                            Log.d("MENU_TEST", "menuPrice : ${data[i].menu?.get(j)?.menuPrice}")
                            Log.d("MENU_TEST", "popularYn : ${data[i].menu?.get(j)?.popularYn}")
                        }
                        Log.d("MENU_TEST", "====================================================")
                    }
                }
            }
        }
    }
}