package com.myoutfit.utils

import kotlinx.coroutines.Dispatchers

object CoroutineContextPool {
    val background = Dispatchers.IO
    val ui = Dispatchers.Main
}
