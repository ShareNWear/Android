package com.myoutfit.di.modules

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.myoutfit.BuildConfig
import com.myoutfit.api.RefreshTokenApi
import com.myoutfit.data.locale.sharedpreferences.AppSharedPreferences
import com.myoutfit.data.remote.interceptors.RefreshTokenInterceptor
import com.myoutfit.data.remote.interceptors.RequestHeaderInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
object NetworkModule {

    @Provides
    @Singleton
    @JvmStatic
    fun provideApplicationApi(
        httpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .client(httpClient)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    @JvmStatic
    fun provideHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        interceptor: RequestHeaderInterceptor,
        refreshTokenInterceptor: RefreshTokenInterceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(refreshTokenInterceptor)
            .retryOnConnectionFailure(true)
            .readTimeout(18, TimeUnit.SECONDS)
            .connectTimeout(18, TimeUnit.SECONDS)

        if (BuildConfig.ENABLE_LOGS) builder.addInterceptor(httpLoggingInterceptor)

        return builder.build()
    }

    @Provides
    @Singleton
    @JvmStatic
    @Named("refresh")
    fun provideRefreshApplicationApi(
        @Named("refresh") httpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .client(httpClient)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    @JvmStatic
    @Named("refresh")
    fun provideRefreshHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .readTimeout(18, TimeUnit.SECONDS)
            .connectTimeout(18, TimeUnit.SECONDS)

        if (BuildConfig.ENABLE_LOGS) builder.addInterceptor(httpLoggingInterceptor)
        return builder.build()
    }

    @Provides
    @Singleton
    @JvmStatic
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideRefreshTokenInterceptor(
        refreshTokenApi: RefreshTokenApi,
        preferences: AppSharedPreferences
    ): RefreshTokenInterceptor {
        return RefreshTokenInterceptor(refreshTokenApi, preferences)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideRequestHeaderInterceptor(preferences: AppSharedPreferences): RequestHeaderInterceptor {
        return RequestHeaderInterceptor(preferences)
    }

    @Singleton
    @Provides
    @JvmStatic
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ")
            .create()
    }
}