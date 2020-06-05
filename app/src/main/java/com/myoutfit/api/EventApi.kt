package com.myoutfit.api

import com.myoutfit.models.events.EventsResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface EventApi {

    @GET("api/user/events")
    fun getEventsAsync(): Deferred<Response<EventsResponse>>
}