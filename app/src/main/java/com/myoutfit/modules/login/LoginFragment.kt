package com.myoutfit.modules.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.myoutfit.R
import com.myoutfit.base.BaseFragment
import com.myoutfit.data.locale.sharedpreferences.AppSharedPreferences
import com.myoutfit.di.AppViewModelsFactory
import com.myoutfit.models.network.ApiRequestStatus
import com.myoutfit.utils.extentions.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.*
import javax.inject.Inject

class LoginFragment : BaseFragment() {

    companion object {
        private const val START_DELAY = 1000L
    }

    @Inject
    lateinit var vmFactory: AppViewModelsFactory

    @Inject
    lateinit var sp: AppSharedPreferences

    private lateinit var viewModel: LoginViewModel
    private lateinit var callbackManager: CallbackManager

    private var initTime: Long? = 0

    override fun layoutId(): Int {
        return R.layout.fragment_login
    }

    override fun onViewReady(inflatedView: View, args: Bundle?) {
        initFacebookLogin()
    }

    override fun setListeners() {
        btnLogin.setOnClickListener {
            LoginManager.getInstance().logOut()
            LoginManager.getInstance().logInWithReadPermissions(
                this@LoginFragment, listOf("user_events")
            )
        }
    }

    override fun initViewModel() {
        viewModel =
            ViewModelProviders.of(this, vmFactory).get(LoginViewModel::class.java)

        viewModel.requestStatusLiveData.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ApiRequestStatus.RUNNING -> {
                    loadView.showWithAnimationAlpha()
                }
                ApiRequestStatus.SUCCESSFUL -> {
                    loadView.goneWithAnimationAlpha()
                }
                ApiRequestStatus.FAILED -> {
                    toastL(it.error?.error ?: getString(R.string.error_server_default))
                    loadView.goneWithAnimationAlpha()
                }
                ApiRequestStatus.NO_INTERNET -> {
                    loadView.goneWithAnimationAlpha()
                    showNoInternetDialog {
                        /*check token status*/
                        viewModel.getProfileData()
                    }
                }
            }
        })

        viewModel.authorizationLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    Navigation.findNavController(requireActivity(), R.id.nav_host).navigate(R.id.action_open_events)
                }
            }
        })

        viewModel.profileDataSuccess.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    /*token is valid - go to the next screen*/
                    /*delay login screen, like splash*/
                    val time = if (initTime != null) {
                        START_DELAY - (System.currentTimeMillis() - initTime!!)
                    } else {
                        START_DELAY
                    }
                    GlobalScope.launch {
                        delay(time)
                        withContext(Dispatchers.Main) {
                            Navigation.findNavController(requireActivity(), R.id.nav_host)
                                .navigate(R.id.action_open_events)
                        }
                    }
                } else {
                    /*token not valid or error received, show login button for relogin*/
                    btnLogin.show()
                }
            }
        })

        checkAccessCode()
    }

    private fun checkAccessCode() {
        if (sp.getAuthKey().isEmpty()) {
            btnLogin.show()
        } else {
            /*check if token expired by fetching profile data*/
            viewModel.getProfileData()
            initTime = System.currentTimeMillis()
        }
    }

    private fun initFacebookLogin() {
        // Initialize Facebook Login button
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    handleFacebookAccessToken(loginResult.accessToken)
                }

                override fun onCancel() {
                    toastL(getString(R.string.error_fb_login))
                }

                override fun onError(error: FacebookException) {
                    error.localizedMessage ?: getString(R.string.error_fb_login).let {
                        toastL(it)
                        logd("Login", it)
                    }
                }
            })
    }

    private fun handleFacebookAccessToken(facebookToken: AccessToken) {
        viewModel.loginFacebook(facebookToken.token)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
