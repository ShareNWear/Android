package com.myoutfit.di

import com.myoutfit.modules.events.EventsViewModel
import com.myoutfit.modules.login.LoginViewModel
import dagger.Subcomponent

@Subcomponent
interface AppViewModelsComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): AppViewModelsComponent
    }

    fun provideLoginViewModel(): LoginViewModel

    fun provideEventsViewModel(): EventsViewModel

}