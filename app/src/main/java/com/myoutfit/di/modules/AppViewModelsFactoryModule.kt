package com.myoutfit.di.modules

import com.myoutfit.di.AppViewModelsComponent
import com.myoutfit.di.AppViewModelsFactory
import dagger.Module
import dagger.Provides

@Module(subcomponents = [AppViewModelsComponent::class])
class AppViewModelsFactoryModule {

    @Provides
    fun provideAppViewModelFactory(builder: AppViewModelsComponent.Builder): AppViewModelsFactory {
        return AppViewModelsFactory(builder.build())
    }
}