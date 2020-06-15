package com.myoutfit.di

import com.myoutfit.modules.eventdetail.EventDetailViewModel
import com.myoutfit.modules.events.EventsViewModel
import com.myoutfit.modules.login.LoginViewModel
import com.myoutfit.modules.myoutfit.MyOutfitViewModel
import dagger.Subcomponent

@Subcomponent
interface AppViewModelsComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): AppViewModelsComponent
    }

    fun provideLoginViewModel(): LoginViewModel

    fun provideEventsViewModel(): EventsViewModel

    fun provideEventDetailViewModel(): EventDetailViewModel

    fun provideMyOutfitViewModel(): MyOutfitViewModel

}