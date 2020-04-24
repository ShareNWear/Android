package com.myoutfit.android.modules

import com.myoutfit.android.AppViewModelsComponent
import com.myoutfit.android.AppViewModelsFactory
import dagger.Module
import dagger.Provides

@Module(subcomponents = [AppViewModelsComponent::class])
class AppViewModelsFactoryModule {

    @Provides
    fun provideAppViewModelFactory(builder: AppViewModelsComponent.Builder): AppViewModelsFactory {
        return AppViewModelsFactory(builder.build())
    }
}