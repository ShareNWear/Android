package com.myoutfit.data.locale.sharedpreferences

import android.content.Context
import com.google.gson.Gson
import javax.inject.Singleton

@Singleton
class SyncSharedPreferences(context: Context, private val gson: Gson) : AppSharedPreferences {

    companion object {
        const val SHARED_PREFERENCES_NAME = "sp_my_outfit"
        const val KEY_AUTH_KEY = " auth_key"
        const val KEY_REFRESH_TOKEN = " refresh_token_key"
    }

    private val sharedPreferences = context.getSharedPreferences(
        SHARED_PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )

    override fun setAuthKey(key: String?) {
        if (key != null) {
            sharedPreferences.edit().putString(KEY_AUTH_KEY, key).apply()
        } else {
            sharedPreferences.edit().putString(KEY_AUTH_KEY, "").apply()
        }
    }

    override fun getAuthKey(): String = sharedPreferences.getString(KEY_AUTH_KEY, "") ?: ""

    override fun clearAuthKey() {
        setAuthKey(null)
    }

    override fun setRefreshToken(key: String) {
        sharedPreferences.edit().putString(KEY_REFRESH_TOKEN, key).apply()
    }

    override fun getRefreshToken(): String = sharedPreferences.getString(KEY_REFRESH_TOKEN, "") ?: ""
}