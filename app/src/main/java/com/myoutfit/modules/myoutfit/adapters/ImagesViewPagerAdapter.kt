package com.myoutfit.modules.myoutfit.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
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
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    fun removeImage(item: ImageAdapterModel) {
        val position = dataList.indexOf(item)
        dataList.removeAt(position)
        notifyDataSetChanged()
    }
}