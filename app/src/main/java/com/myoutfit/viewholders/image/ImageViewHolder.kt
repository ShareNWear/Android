package com.myoutfit.viewholders.image

import android.view.View
import com.myoutfit.R
import com.myoutfit.models.image.ImageAdapterModel
import com.myoutfit.utils.extentions.gone
import com.myoutfit.utils.extentions.loadWithGlide
import com.myoutfit.utils.extentions.show
import com.myoutfit.viewholders.BaseViewHolder
import kotlinx.android.synthetic.main.item_image.view.*

class ImageViewHolder(
    private val onRemoveClicked: (ImageAdapterModel) -> Unit,
    private val isRemovable: Boolean,
    view: View
) : BaseViewHolder<ImageAdapterModel>(view) {

    override fun bind(item: ImageAdapterModel) {
        itemView.ivOutfit.loadWithGlide(item.path, R.drawable.placeholder_friend_blur)
        if (isRemovable) {
            itemView.btnRemove.show()
        } else {
            itemView.btnRemove.gone()
        }
        itemView.btnRemove.setOnClickListener {
            onRemoveClicked(item)
        }
    }
}