package com.myoutfit.models.login

import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @SerializedName("id") val id: String?,
    @SerializedName("phoneNumber") val phoneNumber: String?
)