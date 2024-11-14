package com.samgye.orderapp.utils

import java.text.SimpleDateFormat
import java.util.Locale

class SystemUtil {
    companion object {
        // YYYY.MM.DD
        fun formatToDateOnly(dateString: String): String {
            return try {
                val originalFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS", Locale.getDefault())
                val targetFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
                val date = originalFormat.parse(dateString)
                targetFormat.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }

        // YYYY-MM-DD HH:SS
        fun formatToDateTime(dateString: String): String {
            return try {
                val originalFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS", Locale.getDefault())
                val targetFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                val date = originalFormat.parse(dateString)
                targetFormat.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }
    }
}