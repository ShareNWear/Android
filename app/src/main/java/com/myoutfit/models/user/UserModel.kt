package com.myoutfit.models.user

import com.google.gson.annotations.SerializedName

class UserModel(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") var name: String?,
    @SerializedName("logo_path") val logoPath: String?
)