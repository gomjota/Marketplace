package com.juangomez.presentation.viewmodels

import com.juangomez.presentation.BuildConfig
import com.juangomez.presentation.common.SingleLiveEvent
import com.juangomez.presentation.viewmodels.base.BaseViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : BaseViewModel() {

    val nextView = SingleLiveEvent<SplashState>()

    fun prepare() {
        GlobalScope.launch {
            delay(BuildConfig.DEFAULT_SPLASH_TIME)
            nextView.postValue(SplashState.MainActivity)
        }
    }
}

sealed class SplashState {
    object MainActivity : SplashState()
}