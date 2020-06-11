package com.myoutfit.modules.eventdetail.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myoutfit.R
import com.myoutfit.models.ImageModel
import com.myoutfit.viewholders.outfit.OutfitImageViewHolder

class OutfitImagesAdapter(private val onImageClicked: (model: ImageModel) -> Unit) :
    RecyclerView.Adapter<OutfitImageViewHolder>() {

    private val dataList = mutableListOf<ImageModel>()

    private val tempNames =
        listOf("Jordanna Kitchener", "Cvita Doleschall", "Gopichand Sana", "Martijn Dragonjer")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OutfitImageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_outfit_image, parent, false)
        return OutfitImageViewHolder(view, onImageClicked)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: OutfitImageViewHolder, position: Int) {
        //temp name replace
        if (position != 0 && position < tempNames.size)
            holder.bind(dataList[position], tempNames[position - 1])
        else holder.bind(dataList[position])
    }

    fun setData(data: List<ImageModel>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }
}