package com.myoutfit.modules.login

import androidx.lifecycle.MutableLiveData
import com.myoutfit.base.BaseViewModel
import com.myoutfit.models.network.NetworkState
import com.myoutfit.repositories.AuthorizationRepository
import com.myoutfit.utils.logd
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
                    logd("Login", it.toString())
                    requestStatusLiveData.postValue(NetworkState.SUCCESSFUL)
                    //success

                }, {
                    requestStatusLiveData.postValue(NetworkState.FAILED)
                    logd("Login", "error $it")
                    //error

                }, {
                    logd("Login", it.toString())
                    //network error
                })
        }
    }

}
