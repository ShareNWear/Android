package com.myoutfit.android.modules

import com.myoutfit.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityBuilder {

    @ContributesAndroidInjector
    fun contributeMainActivity(): MainActivity

}