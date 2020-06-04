package com.myoutfit.utils.extentions

import android.app.Activity
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment


fun Fragment.toastL(text: String) {
    Toast.makeText(activity, text, Toast.LENGTH_LONG).show()
}

fun Fragment.toastSh(text: String) {
    Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
}

fun Activity.toastL(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}

fun Activity.toastSh(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun View?.show() {
    this?.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun View?.gone() {
    this?.visibility = View.GONE
}
