package com.myoutfit.modules.login

import androidx.lifecycle.MutableLiveData
import com.myoutfit.base.BaseViewModel
import com.myoutfit.models.network.NetworkState
import com.myoutfit.repositories.AuthorizationRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val authorizationRepository: AuthorizationRepository) : BaseViewModel() {

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
//                        if (it.token != null) {
//                            // authorizationStatusLiveData.postValue(AuthorizationState.NOTREGISTERED)
//                            authorizationLiveData.postValue(true)
//                        } else {
//                            authorizationLiveData.postValue(false)
//                        }
                        requestStatusLiveData.postValue(NetworkState.SUCCESSFUL)
                        //success

                    }, {
                        requestStatusLiveData.postValue(NetworkState.FAILED)
                        authorizationLiveData.postValue(false)
                        //error

                    }, {
//                        requestStatusLiveData.postValue(NetworkState.error(R.string.error_internet_connection))
                        authorizationLiveData.postValue(false)
                        //network error
                    })
            }
        }

}
