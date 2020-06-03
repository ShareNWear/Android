package com.myoutfit.data.locale.sharedpreferences

interface AppSharedPreferences {

    fun setAuthKey(key: String?)
    fun getAuthKey(): String
}