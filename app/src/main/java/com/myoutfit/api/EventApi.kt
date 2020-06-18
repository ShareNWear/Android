package com.myoutfit.api

import com.myoutfit.models.events.EventDetailResponse
import com.myoutfit.models.events.EventsResponse
import com.myoutfit.models.image.MyImagesResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface EventApi {

    @GET("api/user/events")
    fun getEventsAsync(): Deferred<Response<EventsResponse>>

    @GET("api/user/events/{id}")
    fun getEventDetailAsync(
        @Path("id") id: Int
    ): Deferred<Response<EventDetailResponse>>

    @GET("api/user/events/{id}/my-images")
    fun getMyImagesAsync(
        @Path("id") id: Int
    ): Deferred<Response<MyImagesResponse>>

    @DELETE("api/user/events/delete-image/{id}")
    fun deleteImageAsync(
        @Path("id") id: Int
    ): Deferred<Response<Any>>
}