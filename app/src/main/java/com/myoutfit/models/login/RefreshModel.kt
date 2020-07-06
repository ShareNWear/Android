package com.myoutfit.models.login

import com.google.gson.annotations.SerializedName

class RefreshModel(@SerializedName("refresh_token") val token: String)