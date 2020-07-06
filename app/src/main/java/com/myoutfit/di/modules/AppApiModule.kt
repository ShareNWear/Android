package com.myoutfit.di.modules

import com.myoutfit.api.AuthorizationApi
import com.myoutfit.api.EventApi
import com.myoutfit.api.RefreshTokenApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
object AppApiModule {

    @Singleton
    @JvmStatic
    @Provides
    fun provideAuthorizationApi(retrofit: Retrofit): AuthorizationApi =
        retrofit.create(AuthorizationApi::class.java)

    @Singleton
    @JvmStatic
    @Provides
    fun provideEventApi(retrofit: Retrofit): EventApi =
        retrofit.create(EventApi::class.java)

    @Singleton
    @JvmStatic
    @Provides
    fun provideRefreshTokenApi(@Named("refresh") retrofit: Retrofit): RefreshTokenApi =
        retrofit.create(RefreshTokenApi::class.java)
}
