package com.juangomez.marketplace.views.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {

    abstract val layoutId: Int
    abstract val toolbarView: Toolbar

    protected lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.setLifecycleOwner(this)
        configureBinding(binding)
        setSupportActionBar(toolbarView)
        prepare(intent)
    }

    abstract fun configureBinding(binding: T)

    open fun prepare(intent: Intent?) {}
}