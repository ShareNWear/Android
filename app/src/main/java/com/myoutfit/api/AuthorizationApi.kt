package com.myoutfit.api

import com.myoutfit.models.login.LoginModel
import com.myoutfit.models.login.LoginResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthorizationApi {

    @POST("api/user/login")
    fun authorizationAsync(@Body model: LoginModel): Deferred<Response<LoginResponse>>

}