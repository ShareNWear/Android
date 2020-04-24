package com.myoutfit.models.network

import com.google.gson.annotations.SerializedName

data class CommonServerError(
    val errorCode: String,
    @SerializedName("error") val error: String
)