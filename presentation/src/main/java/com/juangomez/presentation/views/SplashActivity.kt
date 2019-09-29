package com.juangomez.presentation.views

import android.os.Bundle
import androidx.lifecycle.Observer
import com.juangomez.presentation.R
import com.juangomez.presentation.databinding.SplashActivityBinding
import com.juangomez.presentation.viewmodels.SplashViewModel
import com.juangomez.presentation.views.base.BaseActivity
import org.koin.android.viewmodel.ext.android.viewModel

class SplashActivity : BaseActivity<SplashActivityBinding>() {

    private val viewModel: SplashViewModel by viewModel()
    override val layoutId: Int = R.layout.splash_activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.nextView.observe(this, Observer { goToNextView(it) })
        viewModel.prepare()
    }

    override fun configureBinding(binding: SplashActivityBinding) {
        binding.viewModel = viewModel
    }

    private fun goToNextView(splashState: SplashViewModel.SplashState) {
        when(splashState) {
            is SplashViewModel.SplashState.MainActivity -> { ProductsActivity.open(this) }
        }
    }
}