package com.myoutfit.utils.extentions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.View

fun View?.showWithAnimationAlpha() {
    this?.let {
        if (it.visibility == View.GONE || it.visibility == View.INVISIBLE) {
            it.visibility = View.VISIBLE
            it.alpha = 0f
            it.animate()
                .alpha(1.0f)
                .setListener(null)
        }
    }
}

fun View?.hideWithAnimationAlpha() {
    this?.let {
        if (it.visibility == View.VISIBLE) {
            it.animate()
                .alpha(0.0f)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        it.visibility = View.INVISIBLE
                        super.onAnimationEnd(animation)
                    }
                })
        }
    }
}

fun View?.goneWithAnimationAlpha() {
    this?.let {
        if (it.visibility == View.VISIBLE) {
            it.animate()
                .alpha(0.0f)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        it.visibility = View.GONE
                        super.onAnimationEnd(animation)
                    }
                })
        }
    }
}

fun View?.animationRotate(degrees: Float) {
    this?.let {
        val rotate = ObjectAnimator.ofFloat(it, "rotation", degrees)
        rotate.duration = 250
        rotate.start()
    }
}
