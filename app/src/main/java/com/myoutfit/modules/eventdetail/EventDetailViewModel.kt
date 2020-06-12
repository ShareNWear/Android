package com.myoutfit.modules.eventdetail

import androidx.lifecycle.MutableLiveData
import com.myoutfit.base.BaseViewModel
import com.myoutfit.models.events.EventDetailResponse
import com.myoutfit.models.network.NetworkState
import com.myoutfit.repositories.EventRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class EventDetailViewModel @Inject constructor(private val eventRepository: EventRepository) : BaseViewModel() {

    val requestStatusLiveData by lazy { MutableLiveData<NetworkState>() }

    val eventLiveData by lazy { MutableLiveData<EventDetailResponse>() }

    fun getEventData(id: Int, showLoader: Boolean) {
        if (showLoader)
            requestStatusLiveData.postValue(NetworkState.LOADING)
        launch {
            eventRepository.getEventDetail(id, {
                requestStatusLiveData.postValue(NetworkState.SUCCESSFUL)
                eventLiveData.postValue(it)
            }, {
                requestStatusLiveData.postValue(NetworkState.FAILED)
            }, {
                requestStatusLiveData.postValue(NetworkState.NO_INTERNET)
            })
        }
    }
}