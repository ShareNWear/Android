package com.myoutfit.di

import androidx.collection.ArrayMap
import androidx.lifecycle.ViewModel
import com.myoutfit.modules.login.LoginViewModel
import java.util.concurrent.Callable

class AppViewModelsFactory(private val appViewModelsComponent: AppViewModelsComponent) :
    BaseViewModelFactory() {

    override fun fillViewModels(creators: ArrayMap<Class<*>, Callable<out ViewModel>>) {
        creators[LoginViewModel::class.java] = Callable { appViewModelsComponent.provideLoginViewModel() }
    }
}
