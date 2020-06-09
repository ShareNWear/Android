package com.myoutfit.utils.extentions

fun String.minTwoDigits(): String {
    return if (this.toInt() < 10) "0$this" else this
}

fun Int.minTwoDigits(): String {
    return if (this < 10) "0$this" else this.toString()
}

fun Long.minTwoDigits(): String {
    return if (this < 10) "0$this" else this.toString()
}