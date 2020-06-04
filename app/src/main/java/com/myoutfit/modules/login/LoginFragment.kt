package com.myoutfit.modules.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.myoutfit.R
import com.myoutfit.base.BaseFragment
import com.myoutfit.di.AppViewModelsFactory
import com.myoutfit.models.network.ApiRequestStatus
import com.myoutfit.utils.extentions.toastL
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

class LoginFragment : BaseFragment() {

    @Inject
    lateinit var vmFactory: AppViewModelsFactory

    private lateinit var viewModel: LoginViewModel
    private lateinit var callbackManager: CallbackManager

    override fun layoutId(): Int {
        return R.layout.fragment_login
    }

    override fun onViewReady(inflatedView: View, args: Bundle?) {
        initFacebookLogin()
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
            ViewModelProviders.of(requireActivity(), vmFactory).get(LoginViewModel::class.java)

        viewModel.requestStatusLiveData.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ApiRequestStatus.RUNNING -> showLoading()
                ApiRequestStatus.SUCCESSFUL -> hideLoading()
                ApiRequestStatus.FAILED -> {
                    toastL(getString(R.string.error_server_default))
                    hideLoading()
                }
                ApiRequestStatus.NO_INTERNET -> {
                    hideLoading()
                    toastL(getString(R.string.error_internet_connection))
                }
            }
        })

        viewModel.authorizationLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    Toast.makeText(context, "Success", Toast.LENGTH_LONG).show()
                }
            }
        })
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
                    toastL(getString(R.string.error_fb_login))
                }
            })
    }

    private fun handleFacebookAccessToken(facebookToken: AccessToken) {
        val tempCode =
            "AQDMGyE9RmcGp1b9ntLRl0iGKgsjd6V9DDnySnRkW7_Az44zDet7vRhoTqyailri2vYOVnfsTtvrpAeCka2v5TjxpRL7OvNLsSO0aRHXkJBehcYAdz21jWh8BCB-KkzfcRXqgiFrePlNYwL6ROHICaYJ9Q0HqwoSbmWKyBSst1DjeaxnkVBg6TH3P5sTalPK-xuYcDMVHyS_DLgIP1P6iaULdNQ_9Cixne5mXrhKroaycFwTqIC47U21kJf0cdtO3SWrVSpe_z-o9giq3A6Mk0SQnWHrfFe2f3N0bNGPbKUx4GykT5zY_8-0ShL3SRj57zvHhvr6vpz2hha6ws-BEwes#_=_"
        viewModel.loginFacebook(tempCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
