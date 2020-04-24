package com.myoutfit.android.modules

import com.myoutfit.api.ApplicationApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
object AppApiModule {

    @Singleton
    @JvmStatic
    @Provides
    fun provideApplicationApi(retrofit: Retrofit): ApplicationApi =
        retrofit.create(ApplicationApi::class.java)

}