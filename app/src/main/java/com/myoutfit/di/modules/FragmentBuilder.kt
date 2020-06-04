package com.myoutfit.di.modules

import com.myoutfit.modules.events.EventsFragment
import com.myoutfit.modules.login.LoginFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentBuilder {

    @ContributesAndroidInjector
    fun provideLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    fun provideEventsFragment(): EventsFragment

}