package com.myoutfit

import android.app.Activity
import android.app.Application
import com.myoutfit.android.AppComponent
import com.myoutfit.android.initInjections
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class MyOutfitApp : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingInjector: DispatchingAndroidInjector<Activity>

    companion object {
        lateinit var instance: MyOutfitApp
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent = initInjections(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> =
        dispatchingInjector

    fun getInstance(): MyOutfitApp {
        return instance
    }
}