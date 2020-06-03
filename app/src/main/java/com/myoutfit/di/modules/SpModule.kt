package com.myoutfit.di.modules

import com.google.gson.Gson
import com.myoutfit.MyOutfitApp
import com.myoutfit.data.locale.sharedpreferences.AppSharedPreferences
import com.myoutfit.data.locale.sharedpreferences.SyncSharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SpModule {

    @Singleton
    @Provides
    fun provideSyncSharedPreferences(app: MyOutfitApp, gson: Gson): AppSharedPreferences =
        SyncSharedPreferences(app, gson)
}