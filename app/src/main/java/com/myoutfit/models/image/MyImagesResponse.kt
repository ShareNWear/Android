package com.myoutfit.models.image

import com.google.gson.annotations.SerializedName

class MyImagesResponse(
    @SerializedName("data") val data: List<ImageModel>?
)