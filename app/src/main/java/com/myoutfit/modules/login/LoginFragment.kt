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
                    Navigation.findNavController(requireActivity(), R.id.nav_host).navigate(R.id.eventsFragment)
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
        /* val tempCode =
             "AQCBl3j8rIkJdYsu-E0YU4QkY_xzn_akYkzWWd8EECwwZlLKGXN4F7byVPXaf3QWJOqhEi22FX0wKlX-pmruf1ucgwfZQD-QMVLisWM2ykak5CinU3Txun_cw-LDW0ZqVFp4BnSn8XQuLBpbW9rV3c3_oyX-EkNeLyZEUFNaGc114v76tVuOPfg75OneQzZ9BH752IEsgLbszrTt76SXBjK1Xak0xhOrBcEgTkfEE3btb1ZUeBoHTKDQR83f332WImEQ6XIlxIF-JzBLF1xo9SaHlGSb6RBecZCczuFkFFROSj39CnY6yHZ5srfnDQu431zJyYbDRjnA45HT7nfnstBP#_=_"
         viewModel.loginFacebook(tempCode)*/
        Navigation.findNavController(requireActivity(), R.id.nav_host).navigate(R.id.action_open_events)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
