package com.myoutfit.repositories

import com.myoutfit.api.AuthorizationApi
import com.myoutfit.data.locale.sharedpreferences.AppSharedPreferences
import com.myoutfit.models.login.LoginModel
import com.myoutfit.models.login.LoginResponse
import com.myoutfit.models.network.NetworkState
import com.myoutfit.utils.safeApiCall
import okhttp3.ResponseBody
import javax.inject.Inject

class AuthorizationRepository @Inject constructor(
    private val authorizationApi: AuthorizationApi,
    private val sp: AppSharedPreferences
) {

    suspend fun loginWithFacebook(
        facebookToken: String,
        onSuccess: suspend (response: LoginResponse) -> Unit,
        onError: suspend (ResponseBody?) -> Unit,
        onNetworkError: (NetworkState) -> Unit
    ) = safeApiCall(
        call = {
            val response = authorizationApi.authorizationAsync(
                LoginModel(facebookToken)
            ).await()

            val data = response.body()

            if (data != null) {
                data.accessToken?.let {
                    sp.setAuthKey(it)
                }
                onSuccess(data)
            } else onError(response.errorBody())
        },
        errorString = NetworkState.NO_INTERNET_CONNECTION
    ) {
        onNetworkError(it)
    }
}