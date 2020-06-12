package com.myoutfit.modules.fullscreen

import android.os.Bundle
import android.view.View
import com.myoutfit.R
import com.myoutfit.base.BaseFragment
import com.myoutfit.models.ImageModel
import com.myoutfit.utils.extentions.loadWithGlide
import com.myoutfit.utils.extentions.loadWithGlideCircleCrop
import kotlinx.android.synthetic.main.fragment_full_screen_image.*

class FullScreenImageFragment : BaseFragment() {
    companion object {
        const val EXT_OUTFIT_IMAGE = "ext_outfit_image"
        const val EXT_NAME = "ext_name"
        const val EXT_FRIEND_IMAGE = "ext_friend_name"

        fun newInstance(image: ImageModel) = FullScreenImageFragment().apply {
            val bundle = Bundle()
            bundle.putString(EXT_OUTFIT_IMAGE, image.path)
            bundle.putString(EXT_FRIEND_IMAGE, image.user?.logoPath)
            bundle.putString(EXT_NAME, image.user?.name)
            this.arguments = bundle
        }
    }

    override fun layoutId(): Int = R.layout.fragment_full_screen_image

    override fun onViewReady(inflatedView: View, args: Bundle?) {
        fillView()
    }

    override fun initViewModel() {

    }

    override fun setListeners() {
        btnBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun fillView() {
        arguments?.getString(EXT_OUTFIT_IMAGE)?.let {
            fullImageView.loadWithGlide(it)
        }
        arguments?.getString(EXT_FRIEND_IMAGE)?.let {
            ivFriend.loadWithGlideCircleCrop(it)
        }
        arguments?.getString(EXT_NAME)?.let {
            tvName.text = it
        }
    }
}