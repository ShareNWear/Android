package com.myoutfit.api

import com.myoutfit.models.login.LoginResponse
import com.myoutfit.models.login.RefreshModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RefreshTokenApi {

    @POST("api/user/refresh-token")
    fun refreshTokenAsync(@Body model: RefreshModel): Call<LoginResponse>
}