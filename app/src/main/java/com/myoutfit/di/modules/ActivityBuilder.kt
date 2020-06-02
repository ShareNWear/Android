package com.myoutfit.di.modules

import com.myoutfit.modules.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityBuilder {

    @ContributesAndroidInjector
    fun contributeMainActivity(): MainActivity

}