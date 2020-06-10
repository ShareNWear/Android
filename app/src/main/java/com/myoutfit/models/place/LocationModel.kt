package com.myoutfit.models.place

import com.google.gson.annotations.SerializedName

class LocationModel(
    @SerializedName("city") val city: String?,
    @SerializedName("country") val country: String?,
    @SerializedName("latitude") val latitude: Double?,
    @SerializedName("longitude") val longitude: Double?,
    @SerializedName("street") val street: String?,
    @SerializedName("zip") val zip: String?
)