package com.myoutfit.di

import com.myoutfit.MyOutfitApp
import com.myoutfit.di.modules.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppViewModelsFactoryModule::class,
        ActivityBuilder::class,
        FragmentBuilder::class,
        NetworkModule::class,
        SpModule::class
    ]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: MyOutfitApp): Builder

        fun build(): AppComponent
    }

    fun inject(myOutfitApp: MyOutfitApp)
}