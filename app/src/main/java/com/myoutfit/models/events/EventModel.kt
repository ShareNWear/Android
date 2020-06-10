package com.myoutfit.models.events

import com.google.gson.annotations.SerializedName
import java.util.*

open class EventModel(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("start_time") val startTime: Date?,
    @SerializedName("facebook_id") val facebookId: Long?,
    @SerializedName("cover_source") val coverSource: String?
)