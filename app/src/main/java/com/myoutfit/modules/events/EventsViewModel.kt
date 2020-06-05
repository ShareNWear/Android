package com.myoutfit.modules.events

import androidx.lifecycle.MutableLiveData
import com.myoutfit.base.BaseViewModel
import com.myoutfit.models.events.EventModel
import com.myoutfit.models.network.NetworkState
import com.myoutfit.repositories.EventRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class EventsViewModel @Inject constructor(private val eventRepository: EventRepository) : BaseViewModel() {

    val requestStatusLiveData by lazy { MutableLiveData<NetworkState>() }

    val eventsLiveData by lazy { MutableLiveData<List<EventModel>>() }


    fun getEvents() {
        requestStatusLiveData.postValue(NetworkState.LOADING)
        launch {
            eventRepository.getEvents({
                requestStatusLiveData.postValue(NetworkState.SUCCESSFUL)
                eventsLiveData.postValue(it.data)
            }, {
                requestStatusLiveData.postValue(NetworkState.FAILED)
            }, {
                requestStatusLiveData.postValue(NetworkState.NO_INTERNET)
            })
        }
    }
}