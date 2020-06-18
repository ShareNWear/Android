package com.myoutfit.modules.myoutfit

import androidx.lifecycle.MutableLiveData
import com.myoutfit.base.BaseViewModel
import com.myoutfit.models.image.ImageModel
import com.myoutfit.models.network.NetworkState
import com.myoutfit.repositories.EventRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class MyOutfitViewModel @Inject constructor(private val eventRepository: EventRepository) : BaseViewModel() {

    val requestStatusLiveData by lazy { MutableLiveData<NetworkState>() }

    val myImagesLiveData by lazy { MutableLiveData<List<ImageModel>>() }

    fun getMyImages(eventId: Int) {
        requestStatusLiveData.postValue(NetworkState.LOADING)
        launch {
            eventRepository.getMyImages(eventId, {
                requestStatusLiveData.postValue(NetworkState.SUCCESSFUL)
                myImagesLiveData.postValue(it.data)
            }, {
                requestStatusLiveData.postValue(NetworkState.FAILED)
            }, {
                requestStatusLiveData.postValue(NetworkState.NO_INTERNET)
            })
        }
    }
}