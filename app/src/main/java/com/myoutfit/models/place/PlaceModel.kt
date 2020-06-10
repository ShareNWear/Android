package com.myoutfit.models.place

import com.google.gson.annotations.SerializedName

class PlaceModel(
    @SerializedName("name") val name: String?,
    @SerializedName("location") val location: LocationModel?,
    @SerializedName("id") val id: Long?
)