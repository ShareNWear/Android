package com.myoutfit.modules.myoutfit.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.myoutfit.R
import com.myoutfit.models.image.ImageAdapterModel
import com.myoutfit.viewholders.image.ImageViewHolder

class ImagesViewPagerAdapter(
    private val onRemoveClicked: (ImageAdapterModel) -> Unit,
    private val isFullScreen: Boolean = false
) : RecyclerView.Adapter<ImageViewHolder>() {

    private val dataList = mutableListOf<ImageAdapterModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(onRemoveClicked, isFullScreen, view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    fun setData(data: List<ImageAdapterModel>) {
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
                            old.imageName == new.imageName
                }

                override fun getOldListSize() = dataList.size

                override fun getNewListSize() = data.size
            })

            dataList.clear()
            dataList.addAll(data)
            diff.dispatchUpdatesTo(this)
        }
    }

    fun removeImage(item: ImageAdapterModel) {
        val position = dataList.indexOf(dataList.find { it.id == item.id })
        dataList.removeAt(position)
        notifyDataSetChanged()
    }
}