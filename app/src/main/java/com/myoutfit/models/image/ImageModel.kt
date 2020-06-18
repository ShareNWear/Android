package com.myoutfit.models.image

import com.google.gson.annotations.SerializedName
import com.myoutfit.models.user.UserModel

class ImageModel(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("path") val path: String?,
    @SerializedName("mime_type") val mimeType: String?,
    @SerializedName("user") val user: UserModel?
)