package com.myoutfit.data.remote.interceptors

import com.myoutfit.data.locale.sharedpreferences.AppSharedPreferences
import okhttp3.Interceptor
import okhttp3.Response

open class RequestHeaderInterceptor(private val preferences: AppSharedPreferences) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        builder.addHeader("Accept", "application/json")
        if (preferences.getAuthKey().isNotEmpty())
            builder.addHeader("Authorization", "Bearer " + preferences.getAuthKey())
        return chain.proceed(builder.build())
    }
}