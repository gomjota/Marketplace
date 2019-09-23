package com.juangomez.presentation.viewmodels

import com.juangomez.presentation.common.SingleLiveEvent
import com.juangomez.presentation.viewmodels.base.BaseViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : BaseViewModel() {

    val nextView = SingleLiveEvent<SplashState>()

    init {
        GlobalScope.launch {
            delay(3000)
            nextView.postValue(SplashState.MainActivity)
        }
    }
}

sealed class SplashState {
    object MainActivity : SplashState()
}