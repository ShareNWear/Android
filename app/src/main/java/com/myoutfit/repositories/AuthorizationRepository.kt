package com.myoutfit.repositories

import com.myoutfit.api.ApplicationApi
import com.myoutfit.models.login.LoginResponse
import com.myoutfit.models.network.NetworkState
import com.myoutfit.utils.safeApiCall
import javax.inject.Inject

class AuthorizationRepository @Inject constructor(private val applicationApi: ApplicationApi) {

    suspend fun loginWithFacebook(
        facebookToken: String,
        onSuccess: suspend (response: LoginResponse) -> Unit,
        onError: suspend (Int) -> Unit,
        onNetworkError: (NetworkState) -> Unit
    ) = safeApiCall(
        call = {
            val response = applicationApi.authorizationAsync(facebookToken).await()

            val data = response.body()

            if (data != null)
                onSuccess(data)
            },
        errorString = NetworkState.NO_INTERNET_CONNECTION
    ) {
        onNetworkError(it)
    }
}