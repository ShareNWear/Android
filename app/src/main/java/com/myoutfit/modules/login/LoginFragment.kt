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
import com.myoutfit.utils.extentions.gone
import com.myoutfit.utils.extentions.logd
import com.myoutfit.utils.extentions.show
import com.myoutfit.utils.extentions.toastL
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

class LoginFragment : BaseFragment() {

    @Inject
    lateinit var vmFactory: AppViewModelsFactory

    @Inject
    lateinit var sp: AppSharedPreferences

    private lateinit var viewModel: LoginViewModel
    private lateinit var callbackManager: CallbackManager

    override fun layoutId(): Int {
        return R.layout.fragment_login
    }

    override fun onViewReady(inflatedView: View, args: Bundle?) {
        initFacebookLogin()
        checkAccessCode()
    }

    override fun setListeners() {
        btnLogin.setOnClickListener {
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
                    loadView.show()
                }
                ApiRequestStatus.SUCCESSFUL -> {
                    loadView.gone()
                }
                ApiRequestStatus.FAILED -> {
                    toastL(getString(R.string.error_server_default))
                    loadView.gone()
                }
                ApiRequestStatus.NO_INTERNET -> {
                    loadView.gone()
                    showNoInternetDialog {

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
    }

    private fun checkAccessCode() {
        if (sp.getAuthKey().isEmpty()) {
            btnLogin.show()
        } else {
            Navigation.findNavController(requireActivity(), R.id.nav_host).navigate(R.id.action_open_events)
        }
    }

    private fun initFacebookLogin() {
        // Initialize Facebook Login button
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    handleFacebookAccessToken(loginResult.accessToken)
                    logd("accessToke", loginResult.accessToken.token)
                }

                override fun onCancel() {
                    toastL(getString(R.string.error_fb_login))
                }

                override fun onError(error: FacebookException) {
                    toastL(getString(R.string.error_fb_login))
                }
            })
    }

    private fun handleFacebookAccessToken(facebookToken: AccessToken) {
        sp.setAuthKey(getString(R.string.temp_access_token))
        // viewModel.loginFacebook(getString(R.string.temp_auth_code))
        Navigation.findNavController(requireActivity(), R.id.nav_host).navigate(R.id.action_open_events)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
