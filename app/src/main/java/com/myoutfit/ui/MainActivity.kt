package com.myoutfit.ui

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.myoutfit.R
import com.myoutfit.base.BaseActivity
import com.myoutfit.modules.login.LoginFragment

import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import javax.inject.Inject

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var loginFragment: LoginFragment = LoginFragment()
        supportFragmentManager.beginTransaction().add(R.id.content, loginFragment)
            .commit()
    }

}
