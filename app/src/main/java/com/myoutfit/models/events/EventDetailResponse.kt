package com.myoutfit.models.events

import com.google.gson.annotations.SerializedName
import com.myoutfit.models.place.PlaceModel
import com.myoutfit.models.user.UserModel
import java.util.*

class EventDetailResponse(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("start_time") val startTime: Date?,
    @SerializedName("facebook_id") val facebookId: Long?,
    @SerializedName("cover_source") val coverSource: String?,
    @SerializedName("users") val users: List<UserModel>?,
    @SerializedName("place") val place: PlaceModel?
)