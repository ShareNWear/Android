package com.myoutfit.utils.extentions

import java.util.*

fun Date.toStringMonthDay(): String {
    val cal = Calendar.getInstance()
    cal.time = this

    return "${cal.get(Calendar.MONTH).plus(1).minTwoDigits()}\n" +
            cal.get(Calendar.DAY_OF_MONTH).minTwoDigits()
}
