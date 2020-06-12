package com.myoutfit.utils.extentions

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.request.RequestOptions
import com.myoutfit.utils.GlideApp

fun ImageView.loadWithGlide(url: String?, placeholderResId: Int? = null) {
    GlideApp.with(context).load(url).also { glideRequest ->
        placeholderResId?.let {
            glideRequest.placeholder(it)
        }
        glideRequest.into(this)
    }
}

fun ImageView.loadWithGlide(url: Drawable?, placeholderResId: Int? = null) {
    GlideApp.with(context).load(url).also { glideRequest ->
        placeholderResId?.let {
            glideRequest.placeholder(it)
        }
        glideRequest.into(this)
    }
}

fun ImageView.loadWithGlide(@DrawableRes resId: Int?, placeholderResId: Int? = null) {
    GlideApp.with(context).load(resId).also { glideRequest ->
        placeholderResId?.let {
            glideRequest.placeholder(it)
        }
        glideRequest.into(this)
    }
}

fun ImageView.loadWithGlideCircleCrop(url: String?, placeholderResId: Int? = null) {
    GlideApp.with(context).load(url).also { glideRequest ->
        placeholderResId?.let {
            glideRequest.placeholder(it)
        }
        glideRequest.apply(RequestOptions.circleCropTransform()).into(this)
    }
}
