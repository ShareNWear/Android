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
import com.myoutfit.utils.logd
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
                this@LoginFragment,
                listOf("email", "public_profile")
            )

        }
    }

    override fun initViewModel() {
        viewModel =
            ViewModelProviders.of(requireActivity(), vmFactory).get(LoginViewModel::class.java)

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

        btnLoginFacebook.setPermissions("email", "public_profile")
        btnLoginFacebook
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    handleFacebookAccessToken(loginResult.accessToken)
                    logd("Login", loginResult.toString() + "\n${loginResult.accessToken}")
                    Toast.makeText(context, "onSuccess", Toast.LENGTH_LONG).show()
                }

                override fun onCancel() {
                    Toast.makeText(context, "onCancel", Toast.LENGTH_LONG).show()
                }

                override fun onError(error: FacebookException) {
                    Toast.makeText(context, "onError", Toast.LENGTH_LONG).show()
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
