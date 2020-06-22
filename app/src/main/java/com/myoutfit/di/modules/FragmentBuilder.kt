package com.myoutfit.di.modules

import com.myoutfit.modules.eventdetail.EventDetailFragment
import com.myoutfit.modules.events.EventsFragment
import com.myoutfit.modules.fullscreen.FullScreenImageFragment
import com.myoutfit.modules.login.LoginFragment
import com.myoutfit.modules.myoutfit.MyOutfitFragment
import com.myoutfit.modules.myoutfit.ConfirmPhotoFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentBuilder {

    @ContributesAndroidInjector
    fun provideLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    fun provideEventsFragment(): EventsFragment

    @ContributesAndroidInjector
    fun provideEventDetailFragment(): EventDetailFragment

    @ContributesAndroidInjector
    fun provideFullScreenImageFragment(): FullScreenImageFragment

    @ContributesAndroidInjector
    fun provideMyOutfirFragment(): MyOutfitFragment

    @ContributesAndroidInjector
    fun provideConfirmPhotoFragment(): ConfirmPhotoFragment
}