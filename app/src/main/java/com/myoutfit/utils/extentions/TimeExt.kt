package com.myoutfit.utils.extentions

import java.util.*

fun Date.toStringMonthDay(): String {
    val cal = Calendar.getInstance()
    cal.time = this
    return "${cal.get(Calendar.MONTH).plus(1).minTwoDigits()}\n" +
            cal.get(Calendar.DAY_OF_MONTH).minTwoDigits()
}

fun Date.toStringDate(): String {
    val cal = Calendar.getInstance()
    cal.time = this
    return "${cal.get(Calendar.MONTH).plus(1).minTwoDigits()} " +
            "${cal.get(Calendar.DAY_OF_MONTH).minTwoDigits()} • " +
            "${cal.get(Calendar.HOUR).minTwoDigits()}:" +
            "${cal.get(Calendar.MINUTE).minTwoDigits()} " +
            if (cal.get(Calendar.AM_PM) == Calendar.AM) "am" else "pm"
}
