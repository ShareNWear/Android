package com.myoutfit.models.error

import com.google.gson.annotations.SerializedName

class ErrorModel(
    @SerializedName("error") val error: Error?
) {
    class Error(
        @SerializedName("code") val code: String?,
        @SerializedName("message") val message: String?
    )
}
