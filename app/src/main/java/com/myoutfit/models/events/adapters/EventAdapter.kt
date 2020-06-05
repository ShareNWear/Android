package com.myoutfit.models.events.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myoutfit.R
import com.myoutfit.models.events.EventModel
import com.myoutfit.viewholders.event.EventViewHolder

class EventAdapter : RecyclerView.Adapter<EventViewHolder>() {

    private val dataList = mutableListOf<EventModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_event, parent, false)
        val layoutParams = view.layoutParams
        layoutParams.height = (parent.height * 0.3).toInt()
        view.layoutParams = layoutParams
        return EventViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    fun setData(data: List<EventModel>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }
}