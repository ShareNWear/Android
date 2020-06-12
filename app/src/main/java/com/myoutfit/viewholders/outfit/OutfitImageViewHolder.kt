package com.myoutfit.viewholders.outfit

import android.view.View
import com.myoutfit.R
import com.myoutfit.models.ImageModel
import com.myoutfit.utils.extentions.loadWithGlide
import com.myoutfit.viewholders.BaseViewHolder
import kotlinx.android.synthetic.main.item_outfit_image.view.*

class OutfitImageViewHolder(view: View, private val onImageClicked: (model: ImageModel) -> Unit) :
    BaseViewHolder<ImageModel>(view) {

    override fun bind(item: ImageModel) {
        itemView.ivOutfit.loadWithGlide(item.path, R.drawable.placeholder_friend_blur)
        itemView.tvName.text = item.user?.name
        itemView.setOnClickListener {
            onImageClicked(item)
        }
    }
}