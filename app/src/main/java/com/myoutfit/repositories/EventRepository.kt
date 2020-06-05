package com.myoutfit.repositories

import com.myoutfit.R
import com.myoutfit.api.EventApi
import com.myoutfit.models.events.EventsResponse
import com.myoutfit.models.network.NetworkState
import com.myoutfit.utils.safeApiCall
import javax.inject.Inject

class EventRepository @Inject constructor(private val eventApi: EventApi) {

    suspend fun getEvents(
        onSuccess: suspend (response: EventsResponse) -> Unit,
        onError: suspend (Int) -> Unit,
        onNetworkError: (NetworkState) -> Unit
    ) = safeApiCall(
        call = {
            val response = eventApi.getEventsAsync().await()

            val data = response.body()

            if (data != null)
                onSuccess(data)
            else onError(R.string.error_server_default)
        },
        errorString = NetworkState.NO_INTERNET_CONNECTION
    ) {
        onNetworkError(it)
    }
}