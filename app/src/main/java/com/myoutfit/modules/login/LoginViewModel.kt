package com.myoutfit.modules.login

import androidx.lifecycle.MutableLiveData
import com.myoutfit.base.BaseViewModel
import com.myoutfit.models.network.NetworkState
import com.myoutfit.repositories.AuthorizationRepository
import com.myoutfit.utils.extentions.logd
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val authorizationRepository: AuthorizationRepository) :
    BaseViewModel() {

    val requestStatusLiveData by lazy { MutableLiveData<NetworkState>() }
    val authorizationLiveData by lazy { MutableLiveData<Boolean>() }

    fun loginFacebook(
        facebookToken: String
    ) {
        requestStatusLiveData.postValue(NetworkState.LOADING)
        launch {
            authorizationRepository.loginWithFacebook(
                facebookToken,
                {
                    //success
                    logd("Login", it.toString())
                    requestStatusLiveData.postValue(NetworkState.SUCCESSFUL)
                    authorizationLiveData.postValue(true)
                }, {
                    //error
                    requestStatusLiveData.postValue(NetworkState.FAILED)
                    authorizationLiveData.postValue(false)
                    logd("Login", "error $it")
                }, {
                    //network error
                    requestStatusLiveData.postValue(NetworkState.NO_INTERNET)
                    authorizationLiveData.postValue(false)
                    logd("Login", it.toString())
                })
        }
    }
}
