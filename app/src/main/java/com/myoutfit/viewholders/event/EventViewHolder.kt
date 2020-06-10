package com.myoutfit.viewholders.event

import android.view.View
import com.myoutfit.models.events.EventModel
import com.myoutfit.utils.extentions.loadWithGlide
import com.myoutfit.utils.extentions.toStringMonthDay
import com.myoutfit.viewholders.BaseViewHolder
import kotlinx.android.synthetic.main.item_event.view.*

class EventViewHolder(view: View, private val onEventClicked: (model: EventModel) -> Unit) :
    BaseViewHolder<EventModel>(view) {

    override fun bind(item: EventModel) {
        itemView.ivEvent.loadWithGlide(item.coverSource)
        itemView.tvName.text = item.name
        itemView.tvDate.text = item.startTime?.toStringMonthDay() ?: "n/a"
        itemView.setOnClickListener {
            onEventClicked(item)
        }
    }
}