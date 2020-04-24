package com.myoutfit.models.network

import com.google.gson.annotations.SerializedName

data class ServerErrorResponse(
    @SerializedName("status") val status: String,
    @SerializedName("code") val code: Int,
    @SerializedName("data") val data: List<CommonServerError?>
)