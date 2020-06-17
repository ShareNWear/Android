package com.myoutfit.models.user

import com.google.gson.annotations.SerializedName
import com.myoutfit.models.ImageModel

class UserModel(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") var name: String?,
    @SerializedName("logo_path") val logoPath: String?,
    @SerializedName("images") val images: List<ImageModel>?
)