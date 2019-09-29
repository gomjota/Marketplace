package com.juangomez.presentation.views.base

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.juangomez.presentation.R
import com.juangomez.presentation.common.toast
import com.juangomez.presentation.views.CheckoutActivity

abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {

    abstract val layoutId: Int

    protected lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
        configureBinding(binding)
        prepare(intent)
    }

    abstract fun configureBinding(binding: T)

    open fun prepare(intent: Intent?) {}

    open fun showError() = runOnUiThread {
        getString(R.string.generic_error).toast(this)
    }
}