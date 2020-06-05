package com.myoutfit.models.events

import com.google.gson.annotations.SerializedName

class EventModel(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("start_time") val startTime: String,
    @SerializedName("facebook_id") val facebookId: String,
    @SerializedName("cover_source") val coverSource: String
)