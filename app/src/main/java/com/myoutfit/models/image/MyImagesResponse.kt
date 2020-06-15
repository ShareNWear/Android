package com.myoutfit.models.image

import com.google.gson.annotations.SerializedName
import com.myoutfit.models.ImageModel

class MyImagesResponse(
    @SerializedName("data") val data: List<ImageModel>?
)