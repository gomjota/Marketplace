package com.juangomez.presentation

import android.app.Application
import com.juangomez.presentation.di.*
import org.koin.android.ext.android.startKoin
import timber.log.Timber
import timber.log.Timber.DebugTree

class MarketplaceApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(DebugTree())
        loadKoin()
    }

    private fun loadKoin() {
        startKoin(
            this,
            listOf(
                remoteModules,
                databaseModules,
                dataModules,
                domainModules,
                presentationModules
            )

        )
    }
}