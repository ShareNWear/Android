package com.myoutfit.di.modules

import android.content.Context
import com.myoutfit.utils.ImageFileHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ImageFileHelperModule {

    @Singleton
    @Provides
    fun provideImageFileHelper(context: Context): ImageFileHelper = ImageFileHelper(context)
}