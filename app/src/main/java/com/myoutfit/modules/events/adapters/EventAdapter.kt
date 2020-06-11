package com.myoutfit.modules.events.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.myoutfit.R
import com.myoutfit.models.events.EventModel
import com.myoutfit.viewholders.event.EventViewHolder

class EventAdapter(private val onEventClicked: (model: EventModel) -> Unit) : RecyclerView.Adapter<EventViewHolder>() {

    private val dataList = mutableListOf<EventModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_event, parent, false)
        val layoutParams = view.layoutParams
        layoutParams.height = (parent.height * 0.3).toInt()
        view.layoutParams = layoutParams
        return EventViewHolder(view, onEventClicked)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    fun setData(data: List<EventModel>) {
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
                            old.startTime == new.startTime &&
                            old.name == new.name &&
                            old.facebookId == new.facebookId &&
                            old.coverSource == new.coverSource
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