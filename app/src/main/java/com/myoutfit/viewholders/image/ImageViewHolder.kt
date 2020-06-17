package com.myoutfit.viewholders.image

import android.view.View
import com.myoutfit.R
import com.myoutfit.models.ImageModel
import com.myoutfit.utils.extentions.loadWithGlide
import com.myoutfit.viewholders.BaseViewHolder
import kotlinx.android.synthetic.main.item_image.view.*

class ImageViewHolder(view: View) : BaseViewHolder<ImageModel>(view) {

    override fun bind(item: ImageModel) {
        itemView.ivOutfit.loadWithGlide(item.path, R.drawable.placeholder_friend_blur)
    }
}