package com.myoutfit.android

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass


@MustBeDocumented
@kotlin.annotation.Target
@kotlin.annotation.Retention
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)