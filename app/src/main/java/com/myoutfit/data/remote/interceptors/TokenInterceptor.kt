package com.myoutfit.data.remote.interceptors

import com.myoutfit.data.locale.sharedpreferences.AppSharedPreferences
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(private val preferencesManager: AppSharedPreferences) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        // set new token if it in header
        val response = chain.proceed(chain.request())
        val newToken = response.header("Authorization")
        if (newToken != null) {
            preferencesManager.setAuthKey(newToken)
        }
        return response
    }
}