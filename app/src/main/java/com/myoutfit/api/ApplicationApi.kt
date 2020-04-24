package com.myoutfit.api

import com.myoutfit.models.login.LoginResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApplicationApi {

    @POST("api/user/login")
    fun authorizationAsync(@Body token: String): Deferred<Response<LoginResponse>>

}