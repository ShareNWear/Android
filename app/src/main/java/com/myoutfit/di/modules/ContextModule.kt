package com.myoutfit.di.modules

import android.content.Context
import com.myoutfit.MyOutfitApp
import dagger.Module
import dagger.Provides

@Module
class ContextModule {

    @Provides
    fun provideBluetooth(application: MyOutfitApp): Context =
        application.applicationContext
}