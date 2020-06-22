package com.myoutfit.repositories

import com.myoutfit.api.EventApi
import com.myoutfit.models.events.EventDetailResponse
import com.myoutfit.models.events.EventsResponse
import com.myoutfit.models.image.MyImagesResponse
import com.myoutfit.models.network.NetworkState
import com.myoutfit.utils.safeApiCall
import kotlinx.coroutines.awaitAll
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import javax.inject.Inject

class EventRepository @Inject constructor(private val eventApi: EventApi) {

    suspend fun getEvents(
        onSuccess: suspend (response: EventsResponse) -> Unit,
        onError: suspend (ResponseBody?) -> Unit,
        onNetworkError: (NetworkState) -> Unit
    ) = safeApiCall(
        call = {
            val response = eventApi.getEventsAsync().await()

            val data = response.body()

            if (data != null)
                onSuccess(data)
            else onError(response.errorBody())
        },
        errorString = NetworkState.NO_INTERNET_CONNECTION
    ) {
        onNetworkError(it)
    }

    suspend fun getEventDetail(
        id: Int,
        onSuccess: suspend (response: EventDetailResponse) -> Unit,
        onError: suspend (ResponseBody?) -> Unit,
        onNetworkError: (NetworkState) -> Unit
    ) = safeApiCall(
        call = {
            val response = eventApi.getEventDetailAsync(id).await()

            val data = response.body()

            if (data != null)
                onSuccess(data)
            else onError(response.errorBody())
        },
        errorString = NetworkState.NO_INTERNET_CONNECTION
    ) {
        onNetworkError(it)
    }

    suspend fun getMyImages(
        eventId: Int,
        onSuccess: suspend (response: MyImagesResponse) -> Unit,
        onError: suspend (ResponseBody?) -> Unit,
        onNetworkError: (NetworkState) -> Unit
    ) = safeApiCall(
        call = {
            val response = eventApi.getMyImagesAsync(eventId).await()

            val data = response.body()

            if (data != null)
                onSuccess(data)
            else onError(response.errorBody())
        },
        errorString = NetworkState.NO_INTERNET_CONNECTION
    ) {
        onNetworkError(it)
    }

    suspend fun deleteImage(
        imageId: Int,
        onSuccess: suspend (response: Any) -> Unit,
        onError: suspend (ResponseBody?) -> Unit,
        onNetworkError: (NetworkState) -> Unit
    ) = safeApiCall(
        call = {
            val response = eventApi.deleteImageAsync(imageId).await()

            val data = response.body()

            if (data != null)
                onSuccess(data)
            else onError(response.errorBody())
        },
        errorString = NetworkState.NO_INTERNET_CONNECTION
    ) {
        onNetworkError(it)
    }

    suspend fun uploadPhoto(
        eventId: Int,
        images: List<MultipartBody.Part?>,
        onSuccess: suspend (response: Any) -> Unit,
        onError: suspend (ResponseBody?) -> Unit,
        onNetworkError: (NetworkState) -> Unit
    ) = safeApiCall(
        call = {

            val defList = images.map {
                eventApi.uploadPhotoAsync(
                    eventId,
                    it
                )
            }
            val responseList = defList.awaitAll()

            var errorBody: ResponseBody? = null
            var success = true
            responseList.forEach {
                if (it.body() == null) {
                    success = false
                    errorBody = it.errorBody()
                    return@forEach
                }
            }
            if (success) {
                onSuccess(success)
            } else {
                onError(errorBody)
            }
        },
        errorString = NetworkState.NO_INTERNET_CONNECTION
    ) {
        onNetworkError(it)
    }
}