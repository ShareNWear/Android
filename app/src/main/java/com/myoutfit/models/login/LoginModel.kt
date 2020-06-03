package com.myoutfit.models.login

import com.google.gson.annotations.SerializedName

class LoginModel(
    @SerializedName("code")
    val code: String
)