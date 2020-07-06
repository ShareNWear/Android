package com.myoutfit.data.locale.sharedpreferences

interface AppSharedPreferences {

    fun setAuthKey(key: String?)
    fun getAuthKey(): String
    fun clearAuthKey()

    fun setRefreshToken(key: String)
    fun getRefreshToken(): String
}