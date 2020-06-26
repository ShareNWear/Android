package com.myoutfit.modules.login

import androidx.lifecycle.MutableLiveData
import com.myoutfit.base.BaseViewModel
import com.myoutfit.models.network.NetworkState
import com.myoutfit.repositories.AuthorizationRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val authorizationRepository: AuthorizationRepository) :
    BaseViewModel() {

    val requestStatusLiveData by lazy { MutableLiveData<NetworkState>() }
    val authorizationLiveData by lazy { MutableLiveData<Boolean>() }
    val profileDataSuccess by lazy { MutableLiveData<Boolean>() }

    fun loginFacebook(
        facebookToken: String
    ) {
        requestStatusLiveData.postValue(NetworkState.LOADING)
        launch {
            authorizationRepository.loginWithFacebook(
                facebookToken,
                {
                    //success
                    requestStatusLiveData.postValue(NetworkState.SUCCESSFUL)
                    authorizationLiveData.postValue(true)
                }, {
                    //error
                    requestStatusLiveData.postValue(NetworkState.error(it))
                    authorizationLiveData.postValue(false)
                }, {
                    //network error
                    requestStatusLiveData.postValue(NetworkState.NO_INTERNET)
                    authorizationLiveData.postValue(false)
                })
        }
    }

    fun getProfileData() {
        launch {
            authorizationRepository.getProfileData(
                {
                    //success
                    profileDataSuccess.postValue(true)
                }, {
                    //error
                    profileDataSuccess.postValue(false)
                }, {
                    //network error
                    requestStatusLiveData.postValue(NetworkState.NO_INTERNET)
                })
        }
    }
}
