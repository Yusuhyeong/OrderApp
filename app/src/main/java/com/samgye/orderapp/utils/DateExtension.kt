package com.samgye.orderapp.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

val cal1: Calendar = Calendar.getInstance()
val cal2: Calendar = Calendar.getInstance()

fun getKstFormat(pattern: String): SimpleDateFormat {
    val sdf = SimpleDateFormat(pattern)
    sdf.timeZone = TimeZone.getTimeZone("KST")
    return sdf
}

fun getKoreanFormat(pattern: String): SimpleDateFormat {
    val sdf = SimpleDateFormat(pattern, Locale.KOREAN)
    sdf.timeZone = TimeZone.getTimeZone("Asia/Seoul")
    return sdf
}

fun Date.isSameDay(and: Date): Boolean {
    cal1.time = this
    cal2.time = and
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
            && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
            && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
}

fun Date.isBetween(from: Date, to: Date): Boolean {
    return from.time <= this.time && this.time <= to.time
}

fun Date.isToday(): Boolean {
    val myCal = Calendar.getInstance()
    myCal.time = this
    val myYear = myCal.get(Calendar.YEAR)
    val myDay = myCal.get(Calendar.DAY_OF_YEAR)

    val nowCal = Calendar.getInstance()
    val nowYear = nowCal.get(Calendar.YEAR)
    val nowDay = nowCal.get(Calendar.DAY_OF_YEAR)

    return nowYear == myYear && nowDay == myDay
}

fun Date.isYesterday(): Boolean {
    val myCal = Calendar.getInstance()
    myCal.time = this
    val myYear = myCal.get(Calendar.YEAR)
    val myDay = myCal.get(Calendar.DAY_OF_YEAR)

    val yesterdayCal = Calendar.getInstance()
    yesterdayCal.add(Calendar.DAY_OF_YEAR, -1)
    val yesterdayYear = yesterdayCal.get(Calendar.YEAR)
    val yesterdayDay = yesterdayCal.get(Calendar.DAY_OF_YEAR)

    return yesterdayYear == myYear && yesterdayDay == myDay
}

fun Date.isLastYear(): Boolean {
    val myCal = Calendar.getInstance()
    myCal.time = this
    val myYear = myCal.get(Calendar.YEAR)
    val myDay = myCal.get(Calendar.DAY_OF_YEAR)

    val lastYearCal = Calendar.getInstance()
    lastYearCal.add(Calendar.YEAR, -1)
    val lastYearYear = lastYearCal.get(Calendar.YEAR)
    val lastYearDay = lastYearCal.get(Calendar.DAY_OF_YEAR)

    return lastYearYear == myYear && lastYearDay == myDay
}

val Date.monthOfYear: Int
    get() {
        val cal = Calendar.getInstance()
        cal.time = this
        return cal.get(Calendar.MONTH)
    }

val Date.dayOfMonth: Int
    get() {
        val cal = Calendar.getInstance()
        cal.time = this
        return cal.get(Calendar.DAY_OF_MONTH)
    }

val Date.dayOfWeek: Int
    get() {
        val cal = Calendar.getInstance()
        cal.time = this
        return cal.get(Calendar.DAY_OF_WEEK)
    }

val Date.millisOfDay: Int
    get() {
        val cal = Calendar.getInstance()
        cal.time = this
        return cal.get(Calendar.MILLISECOND)
    }

val Date.mYear: Int
    get() {
        val cal = Calendar.getInstance()
        cal.time = this
        return cal.get(Calendar.YEAR)
    }


val Date.millis: Long
    get() = this.time

fun Date.toString(pattern: String): String = getKoreanFormat(pattern).format(this)

// 이번달의 dayOfMonth(해당일) Date return
fun Date.withDayOfMonth(dayOfMonth: Int): Date {
    val cal = Calendar.getInstance()
    cal.time = this
    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
    return cal.time
}

// 해당일의 시작시간(시분초를 0으로) Date return
fun Date.startOfDay(): Date {
    val cal = Calendar.getInstance()
    cal.time = this
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.time
}

fun Date.firstDayOfMonth(): Date {
    return this.withDayOfMonth(1).startOfDay()
}

fun Date.endDayOfMonth(): Date {
    return this.plusMonths(1).firstDayOfMonth().minusSecond(1)
}

fun Date.plusDays(days: Int): Date {
    return if (days == 0) {
        this
    } else {
        val cal = Calendar.getInstance()
        cal.time = this
        cal.add(Calendar.DAY_OF_YEAR, days)
        cal.time
    }
}

fun Date.minusDays(days: Int): Date {
//    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//    Log.d("마이너스 데이즈!!000", "${-days} ::: ${sdf.format(this)}")
    return if (days == 0) {
        this
    } else {
        val cal = Calendar.getInstance()
        cal.time = this
        cal.add(Calendar.DAY_OF_YEAR, -days)
//        Log.d("마이너스 데이즈!!111", "${-days} ::: ${sdf.format(cal.time)}")
        cal.time
    }
}

fun Date.minusMinute(minute: Int): Date {
    return if (minute == 0) {
        this
    } else {
        val cal = Calendar.getInstance()
        cal.time = this
        cal.add(Calendar.MINUTE, -minute)
        cal.time
    }
}

fun Date.minusSecond(second: Int): Date {
    return if (second == 0) {
        this
    } else {
        val cal = Calendar.getInstance()
        cal.time = this
        cal.add(Calendar.SECOND, -second)
        cal.time
    }
}

fun Date.plusMonths(months: Int): Date {
//    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//    Log.d("플러스 먼쓰!!000", "${-months} ::: ${sdf.format(this)}")
    return if (months == 0) {
        this
    } else {
        val cal = Calendar.getInstance()
        cal.time = this
        cal.add(Calendar.MONTH, months)
//        Log.d("플러스 먼쓰!!111", "${-months} ::: ${sdf.format(cal.time)}")
        cal.time
    }
}

fun Date.minusMonths(months: Int): Date {
//    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//    Log.d("마이너스 먼쓰!!000", "${-months} ::: ${sdf.format(this)}")
    return if (months == 0) {
        this
    } else {
        val cal = Calendar.getInstance()
        cal.time = this
        cal.add(Calendar.MONTH, -months)
//        Log.d("마이너스 먼쓰!!111", "${-months} ::: ${sdf.format(cal.time)}")
        cal.time
    }
}