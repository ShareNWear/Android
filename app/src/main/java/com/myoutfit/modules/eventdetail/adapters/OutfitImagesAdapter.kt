package com.myoutfit.modules.eventdetail.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.myoutfit.R
import com.myoutfit.models.user.UserModel
import com.myoutfit.viewholders.outfit.OutfitImageViewHolder

class OutfitImagesAdapter(private val onImageClicked: (model: UserModel) -> Unit) :
    RecyclerView.Adapter<OutfitImageViewHolder>() {

    private val dataList = mutableListOf<UserModel>()

//    /*temp fake names*/
//    private val tempNames =
//        listOf("Martijn Dragonjer", "Cvita Doleschall", "Jordanna Kitchener", "Gopichand Sana", "Vlada Maslyak")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OutfitImageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_user_outfit, parent, false)
        return OutfitImageViewHolder(view, onImageClicked)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: OutfitImageViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    fun setData(data: List<UserModel>) {

        if (dataList.isEmpty()) {
            dataList.clear()
            dataList.addAll(data)
            notifyItemRangeInserted(0, dataList.size)
        } else {
            val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return dataList[oldItemPosition].id == data[newItemPosition].id
                }

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ): Boolean {
                    val new = data[newItemPosition]
                    val old = dataList[oldItemPosition]
                    return old.id == new.id &&
                            old.logoPath == new.logoPath &&
                            old.name == new.name/* &&
                            old.images == new.images*/
                }

                override fun getOldListSize() = dataList.size

                override fun getNewListSize() = data.size
            })

            dataList.clear()
            dataList.addAll(data)
            diff.dispatchUpdatesTo(this)
        }
    }
}