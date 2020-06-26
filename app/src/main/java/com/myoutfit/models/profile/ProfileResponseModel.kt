package com.myoutfit.models.profile

import com.google.gson.annotations.SerializedName
import com.myoutfit.models.user.UserModel

class ProfileResponseModel(
    @SerializedName("data") val data: UserModel?
)