package com.myoutfit.android.modules

import com.myoutfit.modules.login.LoginFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentBuilder {

    @ContributesAndroidInjector
    fun provideLoginFragment(): LoginFragment

}