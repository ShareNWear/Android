package com.myoutfit.android

import com.myoutfit.modules.login.LoginViewModel
import dagger.Subcomponent

@Subcomponent
interface AppViewModelsComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): AppViewModelsComponent
    }

    fun provideLoginViewModel(): LoginViewModel

}