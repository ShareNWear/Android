package com.myoutfit.models.events

import com.google.gson.annotations.SerializedName

class EventsResponse(
    @SerializedName("data") val data: List<EventModel>
)