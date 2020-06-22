package com.myoutfit.modules.myoutfit

import androidx.lifecycle.MutableLiveData
import com.myoutfit.base.BaseViewModel
import com.myoutfit.models.image.ImageAdapterModel
import com.myoutfit.models.image.ImageModel
import com.myoutfit.models.network.NetworkState
import com.myoutfit.repositories.EventRepository
import com.myoutfit.utils.extentions.createPartFromStringUri
import kotlinx.coroutines.launch
import javax.inject.Inject

class MyOutfitViewModel @Inject constructor(private val eventRepository: EventRepository) : BaseViewModel() {

    val requestStatusLiveData by lazy { MutableLiveData<NetworkState>() }

    val myImagesLiveData by lazy { MutableLiveData<List<ImageModel>>() }

    val deletedImageIdLiveData by lazy { MutableLiveData<ImageAdapterModel>() }

    val uploadImageStatus by lazy { MutableLiveData<Boolean>() }

    val imageListLiveData by lazy { MutableLiveData<List<String>>() }

    fun getMyImages(eventId: Int) {
        requestStatusLiveData.postValue(NetworkState.LOADING)
        launch {
            eventRepository.getMyImages(eventId, {
                requestStatusLiveData.postValue(NetworkState.SUCCESSFUL)
                myImagesLiveData.postValue(it.data)
            }, {
                myImagesLiveData.postValue(null)
                requestStatusLiveData.postValue(NetworkState.FAILED)
            }, {
                myImagesLiveData.postValue(null)
                requestStatusLiveData.postValue(NetworkState.NO_INTERNET)
            })
        }
    }

    fun removePhoto(model: ImageAdapterModel) {
        model.id?.let {
            requestStatusLiveData.postValue(NetworkState.LOADING)
            launch {
                eventRepository.deleteImage(model.id, {
                    requestStatusLiveData.postValue(NetworkState.SUCCESSFUL)
                    deletedImageIdLiveData.postValue(model)
                }, {
                    requestStatusLiveData.postValue(NetworkState.FAILED)
                }, {
                    requestStatusLiveData.postValue(NetworkState.NO_INTERNET)
                })
            }
        }
    }

    fun uploadPhoto(eventId: Int) {
        imageListLiveData.value?.let { images ->
            requestStatusLiveData.postValue(NetworkState.LOADING)
            launch {
                eventRepository.uploadPhoto(
                    eventId,
                    images.map {
                        createPartFromStringUri(
                            "image",
                            "image/png",
                            it
                        )
                    }
                    , {
                        requestStatusLiveData.postValue(NetworkState.SUCCESSFUL)
                        uploadImageStatus.postValue(true)
                    }, {
                        requestStatusLiveData.postValue(NetworkState.FAILED)
                        uploadImageStatus.postValue(false)
                    }, {
                        requestStatusLiveData.postValue(NetworkState.NO_INTERNET)
                        uploadImageStatus.postValue(false)
                    })
            }
        }
    }
}