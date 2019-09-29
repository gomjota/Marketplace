package com.juangomez.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.juangomez.presentation.common.SingleLiveEvent
import com.juangomez.presentation.viewmodels.SplashState
import com.juangomez.presentation.viewmodels.SplashViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.timeout
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.Config


@RunWith(JUnit4::class)
class SplashViewModelTest {

    private lateinit var viewModel: SplashViewModel
    private lateinit var nextView: SingleLiveEvent<SplashState>

    @Mock
    lateinit var observer: Observer<SplashState>

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewModel = SplashViewModel()
        nextView = viewModel.nextView
    }

    @Test
    fun `next view should be triggered after delay`() {
        val margin = BuildConfig.DEFAULT_SPLASH_TIME + 2000
        viewModel.nextView.observeForever(observer)
        viewModel.prepare()
        verify(observer, timeout(margin).times(1)).onChanged(SplashState.MainActivity)
    }
}