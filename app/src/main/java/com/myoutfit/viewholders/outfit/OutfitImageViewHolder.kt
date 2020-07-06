package com.myoutfit.viewholders.outfit

import android.view.View
import com.myoutfit.R
import com.myoutfit.models.user.UserModel
import com.myoutfit.utils.extentions.loadWithGlide
import com.myoutfit.viewholders.BaseViewHolder
import kotlinx.android.synthetic.main.item_user_outfit.view.*

class OutfitImageViewHolder(view: View, private val onImageClicked: (model: UserModel) -> Unit) :
    BaseViewHolder<UserModel>(view) {

    override fun bind(item: UserModel) {
        itemView.ivOutfit.loadWithGlide(item.images?.getOrNull(0)?.path, R.drawable.placeholder_friend_blur)
        item.images?.size?.let {
            val currentItemCount = "${1}/${it}"
            itemView.tvImageCount.text = currentItemCount
        }
        itemView.tvName.text = item.name
        itemView.setOnClickListener {
            onImageClicked(item)
        }
    }
}