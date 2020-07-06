package com.myoutfit.data.remote.interceptors

import com.myoutfit.api.RefreshTokenApi
import com.myoutfit.data.locale.sharedpreferences.AppSharedPreferences
import com.myoutfit.models.login.RefreshModel
import com.myoutfit.utils.extentions.logd
import okhttp3.Interceptor
import okhttp3.Response

class RefreshTokenInterceptor(
    private val refreshTokenApi: RefreshTokenApi,
    private val sp: AppSharedPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response? {
        val originalRequest = chain.request()
        val initialResponse = chain.proceed(originalRequest)

        when {
            initialResponse.code() == 403 || initialResponse.code() == 401 -> {
                val refreshToken = sp.getRefreshToken()
                if (refreshToken.isNotBlank()) {
                    val response =
                        refreshTokenApi.refreshTokenAsync(RefreshModel(refreshToken))
                            .execute()
                    return when {
                        response.code() != 200 -> {
                            /*refresh failed*/
                            logd("refresh", "failed refresh " + response.message())
                            chain.proceed(originalRequest)
                        }
                        else -> {
                            sp.setAuthKey(response.body()?.accessToken)
                            response.body()?.refreshToken?.let {
                                sp.setRefreshToken(it)
                            }
                            val newAuthenticationRequest = originalRequest.newBuilder()
                                .removeHeader("Authorization")
                                .addHeader("Authorization", "Bearer " + response.body()?.accessToken)
                                .build()
                            chain.proceed(newAuthenticationRequest)
                        }
                    }
                } else {
                    logd("refresh", refreshToken)
                    return initialResponse
                }
            }

            else -> {
                logd("refresh", initialResponse.code().toString())
                return initialResponse
            }
        }
    }
}